package com.example.personalfinancetracker.features.settings

import app.cash.turbine.test
import com.example.conversion_rate.domain.usecase.ConvertCurrencyUseCase
import com.example.conversion_rate.domain.usecase.GetProvidersUseCase
import com.example.conversion_rate.domain.usecase.InitializeRateSyncUseCase
import com.example.conversion_rate.domain.usecase.ObserveSyncStatusUseCase
import com.example.conversion_rate.sync.SyncStatus
import com.example.core.common.UiText
import com.example.core.model.DefaultCurrencies
import com.example.personalfinancetracker.MainDispatcherRule
import com.example.personalfinancetracker.fakes.FakeExchangeRateRepository
import com.example.personalfinancetracker.fakes.FakeExchangeRateRepository.Companion.PROVIDER_ERROR
import com.example.personalfinancetracker.fakes.FakeRateSyncManager
import com.example.personalfinancetracker.fakes.FakeUserPreferencesRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val dispatcherRule = MainDispatcherRule(testDispatcher)

    private lateinit var userPreferencesRepository: FakeUserPreferencesRepository
    private lateinit var fakeExchangeRepo: FakeExchangeRateRepository
    private lateinit var fakeRateSyncManager: FakeRateSyncManager
    private lateinit var convertCurrencyUseCase: ConvertCurrencyUseCase
    private lateinit var initializeRateSyncUseCase: InitializeRateSyncUseCase
    private lateinit var getProvidersUseCase: GetProvidersUseCase
    private lateinit var observeSyncStatusUseCase: ObserveSyncStatusUseCase

    @Before
    fun setup() {
        userPreferencesRepository = FakeUserPreferencesRepository()
        fakeExchangeRepo = FakeExchangeRateRepository()
        fakeRateSyncManager = FakeRateSyncManager()
        convertCurrencyUseCase = ConvertCurrencyUseCase(fakeExchangeRepo)
        initializeRateSyncUseCase = InitializeRateSyncUseCase(fakeRateSyncManager)
        getProvidersUseCase = GetProvidersUseCase(fakeExchangeRepo)
        observeSyncStatusUseCase = ObserveSyncStatusUseCase(fakeExchangeRepo)
    }

    private fun createViewModel() = SettingsViewModel(
        userPreferencesRepository = userPreferencesRepository,
        convertCurrency = convertCurrencyUseCase,
        initializeRateSync = initializeRateSyncUseCase,
        getProviders = getProvidersUseCase,
        observeSyncStatus = observeSyncStatusUseCase,
    )

    // ── Initial State ──────────────────────────────────────────────

    @Test
    fun `initial state has USD selected and all currencies available`() = runTest {
        val viewModel = createViewModel()

        val state = viewModel.uiState.value
        assertEquals(DefaultCurrencies.USD, state.selectedCurrency)
        assertEquals(DefaultCurrencies.all, state.availableCurrencies)
        assertEquals("100", state.conversionAmount)
        assertNull(state.targetCurrency)
        assertNull(state.conversionResult)
        assertFalse(state.isConverting)
        assertNull(state.conversionError)
        assertEquals(SyncStatus.Idle, state.syncStatus)
    }

    // ── Initialization (getInitialPreferences) ─────────────────────

    @Test
    fun `init loads saved base currency from preferences`() = runTest {
        userPreferencesRepository.setBaseCurrency("EUR")
        fakeExchangeRepo.setFakeProvider("provider1", "provider1")

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(DefaultCurrencies.EUR, viewModel.uiState.value.selectedCurrency)
    }

    @Test
    fun `init loads providers and selects saved provider`() = runTest {
        fakeExchangeRepo.setFakeProvider("provider1", "Provider One")
        userPreferencesRepository.setSelectedProviderId("provider1")

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.availableProviders.size)
        assertEquals("provider1", state.selectedProviderId)
    }

    @Test
    fun `init falls back to first provider when saved provider not found`() = runTest {
        fakeExchangeRepo.setFakeProvider("provider1", "Provider One")
        userPreferencesRepository.setSelectedProviderId("non_existent_provider")

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("provider1", state.selectedProviderId)
    }

    @Test
    fun `init observes sync status changes`() = runTest {
        fakeExchangeRepo.setFakeProvider("provider1", "provider1")

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(SyncStatus.Idle, viewModel.uiState.value.syncStatus)
    }

    // ── Event: OnNavigateBack ──────────────────────────────────────

    @Test
    fun `OnNavigateBack triggers NavigateBack side effect`() = runTest {
        fakeExchangeRepo.setFakeProvider("provider1", "provider1")
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.sideEffect.test {
            viewModel.onEvent(SettingsEvent.OnNavigateBack)
            assertEquals(SettingsSideEffect.NavigateBack, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ── Event: OnCurrencySelected ──────────────────────────────────

    @Test
    fun `OnCurrencySelected updates base currency in preferences and triggers sync`() = runTest {
        fakeExchangeRepo.setFakeProvider("provider1", "provider1")
        userPreferencesRepository.setSelectedProviderId("provider1")

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(SettingsEvent.OnCurrencySelected(DefaultCurrencies.EUR))
        advanceUntilIdle()

        // The ViewModel collects baseCurrency flow, so state should reflect EUR
        assertEquals(DefaultCurrencies.EUR, viewModel.uiState.value.selectedCurrency)

        // Verify sync was triggered
        assertTrue(fakeRateSyncManager.immediateSyncCalls.any { it.baseCurrency == "EUR" })
        assertTrue(fakeRateSyncManager.periodicSyncCalls.any { it.baseCurrency == "EUR" })
    }

    // ── Event: OnAmountChanged ─────────────────────────────────────

    @Test
    fun `OnAmountChanged updates amount and clears previous result and error`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(SettingsEvent.OnAmountChanged("250.50"))

        val state = viewModel.uiState.value
        assertEquals("250.50", state.conversionAmount)
        assertNull(state.conversionResult)
        assertNull(state.conversionError)
    }

    // ── Event: OnTargetCurrencySelected ───────────────────────────

    @Test
    fun `OnTargetCurrencySelected updates target and clears previous result and error`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(SettingsEvent.OnTargetCurrencySelected(DefaultCurrencies.GBP))

        val state = viewModel.uiState.value
        assertEquals(DefaultCurrencies.GBP, state.targetCurrency)
        assertNull(state.conversionResult)
        assertNull(state.conversionError)
    }

    // ── Event: OnProviderSelected ─────────────────────────────────

    @Test
    fun `OnProviderSelected updates provider and triggers sync`() = runTest {
        fakeExchangeRepo.setFakeProvider("provider1", "provider1")
        userPreferencesRepository.setBaseCurrency("USD")

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(SettingsEvent.OnProviderSelected("provider1"))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("provider1", state.selectedProviderId)
        assertNull(state.conversionResult)
        assertNull(state.conversionError)

        // Verify sync was triggered with the new provider
        assertTrue(fakeRateSyncManager.immediateSyncCalls.any { it.providerId == "provider1" })
    }

    // ── Event: OnConvertClicked ───────────────────────────────────

    @Test
    fun `OnConvertClicked performs successful conversion`() = runTest {
        fakeExchangeRepo.setFakeProvider("provider1", "provider1")
        fakeExchangeRepo.setFakeRate("USD", "EUR", BigDecimal("0.85"))
        userPreferencesRepository.setBaseCurrency("USD")
        userPreferencesRepository.setSelectedProviderId("provider1")

        val viewModel = createViewModel()
        advanceUntilIdle()

        // Set up conversion parameters
        viewModel.onEvent(SettingsEvent.OnTargetCurrencySelected(DefaultCurrencies.EUR))
        viewModel.onEvent(SettingsEvent.OnAmountChanged("100"))
        viewModel.onEvent(SettingsEvent.OnProviderSelected("provider1"))
        advanceUntilIdle()

        // Perform conversion
        viewModel.onEvent(SettingsEvent.OnConvertClicked)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isConverting)
        assertNotNull(state.conversionResult)
        assertEquals("85.00", state.conversionResult)
        assertNull(state.conversionError)
    }

    @Test
    fun `OnConvertClicked with failed provider shows conversion error`() = runTest {
        fakeExchangeRepo.setFakeProvider("provider1", "provider1")
        userPreferencesRepository.setBaseCurrency("USD")
        userPreferencesRepository.setSelectedProviderId("provider1")

        val viewModel = createViewModel()
        advanceUntilIdle()

        // Select a provider that will fail (provider2 not registered as "provider1" name)
        viewModel.onEvent(SettingsEvent.OnTargetCurrencySelected(DefaultCurrencies.EUR))
        viewModel.onEvent(SettingsEvent.OnAmountChanged("100"))

        // Register provider2 but with a non-"provider1" name so convert() returns failure
        fakeExchangeRepo.setFakeProvider("provider2", "provider2")
        viewModel.onEvent(SettingsEvent.OnProviderSelected("provider2"))
        advanceUntilIdle()

        viewModel.onEvent(SettingsEvent.OnConvertClicked)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isConverting)
        assertNull(state.conversionResult)
        assertNotNull(state.conversionError)
        assertTrue(state.conversionError is UiText.DynamicString)
        assertEquals(PROVIDER_ERROR, (state.conversionError as UiText.DynamicString).value)
    }

    @Test
    fun `OnConvertClicked without target currency does nothing`() = runTest {
        fakeExchangeRepo.setFakeProvider("provider1", "provider1")
        userPreferencesRepository.setSelectedProviderId("provider1")

        val viewModel = createViewModel()
        advanceUntilIdle()

        // Do NOT set target currency
        viewModel.onEvent(SettingsEvent.OnAmountChanged("100"))
        viewModel.onEvent(SettingsEvent.OnConvertClicked)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isConverting)
        assertNull(state.conversionResult)
        assertNull(state.conversionError)
    }

    @Test
    fun `OnConvertClicked without provider does nothing`() = runTest {
        // Don't register any providers, so selectedProviderId remains null
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(SettingsEvent.OnTargetCurrencySelected(DefaultCurrencies.EUR))
        viewModel.onEvent(SettingsEvent.OnAmountChanged("100"))
        viewModel.onEvent(SettingsEvent.OnConvertClicked)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isConverting)
        assertNull(state.conversionResult)
    }

    @Test
    fun `OnConvertClicked with invalid amount does nothing`() = runTest {
        fakeExchangeRepo.setFakeProvider("provider1", "provider1")
        userPreferencesRepository.setSelectedProviderId("provider1")

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(SettingsEvent.OnTargetCurrencySelected(DefaultCurrencies.EUR))
        viewModel.onEvent(SettingsEvent.OnAmountChanged("not_a_number"))
        viewModel.onEvent(SettingsEvent.OnProviderSelected("provider1"))
        advanceUntilIdle()

        viewModel.onEvent(SettingsEvent.OnConvertClicked)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isConverting)
        assertNull(state.conversionResult)
    }

    @Test
    fun `OnConvertClicked with same currency returns same amount`() = runTest {
        fakeExchangeRepo.setFakeProvider("provider1", "provider1")
        userPreferencesRepository.setBaseCurrency("USD")
        userPreferencesRepository.setSelectedProviderId("provider1")

        val viewModel = createViewModel()
        advanceUntilIdle()

        // Target same as base
        viewModel.onEvent(SettingsEvent.OnTargetCurrencySelected(DefaultCurrencies.USD))
        viewModel.onEvent(SettingsEvent.OnAmountChanged("100"))
        viewModel.onEvent(SettingsEvent.OnProviderSelected("provider1"))
        advanceUntilIdle()

        viewModel.onEvent(SettingsEvent.OnConvertClicked)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isConverting)
        assertNotNull(state.conversionResult)
        // Same currency → same amount, scaled to 2 decimal places
        assertEquals("100.00", state.conversionResult)
    }
}