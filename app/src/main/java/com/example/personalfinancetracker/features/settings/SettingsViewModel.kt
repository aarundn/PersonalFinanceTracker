package com.example.personalfinancetracker.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conversion_rate.domain.usecase.ConvertCurrencyUseCase
import com.example.conversion_rate.domain.usecase.GetProvidersUseCase
import com.example.conversion_rate.domain.usecase.InitializeRateSyncUseCase
import com.example.core.model.Currency
import com.example.core.model.DefaultCurrencies
import com.example.domain.repo.UserPreferencesRepository
import com.example.personalfinancetracker.features.settings.SettingsContract.Event
import com.example.personalfinancetracker.features.settings.SettingsContract.SettingsState
import com.example.personalfinancetracker.features.settings.SettingsContract.SideEffect
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.RoundingMode

class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val convertCurrency: ConvertCurrencyUseCase,
    private val initializeRateSync: InitializeRateSyncUseCase,
    private val getProviders: GetProvidersUseCase,
) : ViewModel() {

    private val _sideEffect = MutableSharedFlow<SideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private val _pickerVisible = MutableStateFlow(false)
    private val _conversionState = MutableStateFlow(ConversionTestState())


    val state: StateFlow<SettingsState> =
        combine(
            userPreferencesRepository.baseCurrency,
            _pickerVisible,
            _conversionState,
        ) { currencyId, pickerVisible, conversion ->

            SettingsState(
                selectedCurrency = DefaultCurrencies.fromId(currencyId) ?: DefaultCurrencies.USD,
                availableCurrencies = DefaultCurrencies.all,
                isCurrencyPickerVisible = pickerVisible,
                conversionAmount = conversion.amount,
                targetCurrency = conversion.targetCurrency,
                conversionResult = conversion.result,
                isConverting = conversion.isConverting,
                conversionError = conversion.error,
                availableProviders = conversion.providers,
                selectedProviderId = conversion.selectedProviderId,
                selectedProviderName = conversion.providers
                    .firstOrNull { it.first == conversion.selectedProviderId }?.second,
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SettingsState(
                    selectedCurrency = DefaultCurrencies.USD,
                    availableCurrencies = DefaultCurrencies.all,
                )
            )

    init {
        viewModelScope.launch {
            val savedProviderId = userPreferencesRepository.selectedProviderId.first()
            getProviders().onSuccess { providers ->
                _conversionState.update {
                    it.copy(
                        providers = providers,
                        selectedProviderId = providers
                            .firstOrNull { (id, _) -> id == savedProviderId }?.first
                            ?: providers.firstOrNull()?.first,
                    )
                }
            }
        }
    }

    fun onEvent(event: Event) {
        when (event) {
            Event.OnClickCurrencyPicker -> _pickerVisible.value = true
            Event.OnDismissCurrencyPicker -> _pickerVisible.value = false
            is Event.OnCurrencySelected -> selectCurrency(event.currency)
            Event.OnNavigateBack -> viewModelScope.launch {
                _sideEffect.emit(SideEffect.NavigateBack)
            }

            is Event.OnAmountChanged -> _conversionState.update {
                it.copy(amount = event.amount, result = null, error = null)
            }

            is Event.OnTargetCurrencySelected -> _conversionState.update {
                it.copy(targetCurrency = event.currency, result = null, error = null)
            }

            is Event.OnProviderSelected -> {
                _conversionState.update {
                    it.copy(selectedProviderId = event.providerId, result = null, error = null)
                }
                viewModelScope.launch {
                    userPreferencesRepository.setSelectedProviderId(event.providerId)
                    initializeRateSync(
                        userPreferencesRepository.baseCurrency.first(),
                        event.providerId
                    )
                }
            }

            Event.OnConvertClicked -> performConversion()
        }
    }

    private fun selectCurrency(currency: Currency) {
        viewModelScope.launch {
            userPreferencesRepository.setBaseCurrency(currency.id)
            _pickerVisible.value = false
            val providerId = userPreferencesRepository.selectedProviderId.first()
            initializeRateSync(currency.id, providerId)
        }
    }

    private fun performConversion() {
        val current = _conversionState.value
        val providerId = current.selectedProviderId ?: return
        val target = current.targetCurrency ?: return
        val amount = current.amount.toBigDecimalOrNull() ?: return

        _conversionState.update { it.copy(isConverting = true, result = null, error = null) }

        viewModelScope.launch {
            val baseCurrency = state.value.selectedCurrency.id

            convertCurrency(
                providerId = providerId,
                fromCurrencyCode = baseCurrency,
                toCurrencyCode = target.id,
                amount = amount,
            ).onSuccess { result ->
                _conversionState.update {
                    it.copy(
                        isConverting = false,
                        result = result.setScale(2, RoundingMode.HALF_UP).toPlainString(),
                    )
                }
            }.onFailure { error ->
                _conversionState.update {
                    it.copy(
                        isConverting = false,
                        error = error.message ?: "Conversion failed",
                    )
                }
            }
        }
    }

    private data class ConversionTestState(
        val amount: String = "100",
        val targetCurrency: Currency? = null,
        val result: String? = null,
        val isConverting: Boolean = false,
        val error: String? = null,
        val providers: List<Pair<String, String>> = emptyList(),
        val selectedProviderId: String? = null,
    )
}
