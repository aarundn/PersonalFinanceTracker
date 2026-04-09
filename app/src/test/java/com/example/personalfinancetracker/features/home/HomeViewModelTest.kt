package com.example.personalfinancetracker.features.home

import app.cash.turbine.test
import com.example.conversion_rate.domain.usecase.ConvertCurrencyUseCase
import com.example.core.common.UiText
import com.example.core.di.CoroutineDispatchers
import com.example.domain.usecase.budget_usecases.GetBudgetsUseCase
import com.example.domain.usecase.transaction_usecases.GetTransactionsUseCase
import com.example.personalfinancetracker.MainDispatcherRule
import com.example.personalfinancetracker.fakes.FakeBudgetRepository
import com.example.personalfinancetracker.fakes.FakeClock
import com.example.personalfinancetracker.fakes.FakeExchangeRateRepository
import com.example.personalfinancetracker.fakes.FakeExchangeRateRepository.Companion.PROVIDER_ERROR
import com.example.personalfinancetracker.fakes.FakeTransactionRepository
import com.example.personalfinancetracker.fakes.FakeUserPreferencesRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

class HomeViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    @get:Rule
    val dispatcherRule = MainDispatcherRule(testDispatcher)


    private lateinit var getTransactionsUseCase: GetTransactionsUseCase
    private lateinit var getBudgetsUseCase: GetBudgetsUseCase
    private lateinit var transactionRepository: FakeTransactionRepository
    private lateinit var budgetRepository: FakeBudgetRepository
    private lateinit var userPreferencesRepository: FakeUserPreferencesRepository
    private lateinit var convertCurrencyUseCase: ConvertCurrencyUseCase
    private lateinit var fakeExchangeRepo: FakeExchangeRateRepository
    private lateinit var clock: FakeClock


    @Before
    fun setup() {

        transactionRepository = FakeTransactionRepository()
        budgetRepository = FakeBudgetRepository()
        getBudgetsUseCase = GetBudgetsUseCase(budgetRepository = budgetRepository)
        getTransactionsUseCase =
            GetTransactionsUseCase(transactionRepository = transactionRepository)
        userPreferencesRepository = FakeUserPreferencesRepository()
        fakeExchangeRepo = FakeExchangeRateRepository()
        convertCurrencyUseCase = ConvertCurrencyUseCase(fakeExchangeRepo)
        clock = FakeClock()


    }

    private val testDispatchersBucket = CoroutineDispatchers(
        main = testDispatcher,
        io = testDispatcher,
        default = testDispatcher
    )

    private fun createViewModel() = HomeViewModel(
        getTransactionsUseCase = getTransactionsUseCase,
        userPreferencesRepository = userPreferencesRepository,
        convertCurrencyUseCase = convertCurrencyUseCase,
        clock = clock,
        getBudgetsUseCase = getBudgetsUseCase,
        dispatcher = testDispatchersBucket
    )


    @Test
    fun `homeUiState emits Loading initially`() = runTest {
        val viewModel = createViewModel()
        viewModel.homeUiState.test {
            val currentState = awaitItem()
            assertTrue(currentState is HomeUiState.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `homeUiState emits Error when exception occurs`() = runTest {

        transactionRepository.setShouldThrowError(true)

        val viewModel = createViewModel()
        viewModel.homeUiState.test {

            awaitItem()

            val errorState = awaitItem() as HomeUiState.Error
            assertTrue(errorState.message is UiText.DynamicString)
            assertEquals("Test Exception", (errorState.message as UiText.DynamicString).value)

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `onEvent emits right side effect`() = runTest {
        val viewModel = createViewModel()
        viewModel.sideEffect.test {
            viewModel.onEvent(HomeEvent.OnClickAddExpense)
            assertEquals(HomeSideEffect.NavigateAddExpense, awaitItem())
            viewModel.onEvent(HomeEvent.OnClickAddIncome)
            assertEquals(HomeSideEffect.NavigateAddIncome, awaitItem())
            viewModel.onEvent(HomeEvent.OnClickCurrency)
            assertEquals(HomeSideEffect.NavigateCurrency, awaitItem())
            viewModel.onEvent(HomeEvent.OnClickSettings)
            assertEquals(HomeSideEffect.NavigateSettings, awaitItem())
            viewModel.onEvent(HomeEvent.OnClickSavings)
            assertEquals(
                HomeSideEffect.ShowMessage(UiText.DynamicString("Savings tapped")),
                awaitItem()
            )
        }
    }


    @Test
    fun `homeUiState computes correct totals and converts currencies`() = runTest {

        transactionRepository.shouldHaveFilledList(true)
        budgetRepository.shouldHaveFilledList(true)


        userPreferencesRepository.setBaseCurrency("EUR")
        userPreferencesRepository.setSelectedProviderId("provider1")
        fakeExchangeRepo.setFakeProvider("provider1", "provider1")


        val baseCurrency = userPreferencesRepository.baseCurrency

        fakeExchangeRepo.setFakeRate(
            "DZD",
            baseCurrency.first(),
            BigDecimal("0.8")
        )

        val viewModel = createViewModel()
        viewModel.homeUiState.test {

             awaitItem()
            val successState = awaitItem() as HomeUiState.Success
            val data = successState.data


            // Income = 5000 USD. 5000 * 0.8 = 4000.0 EUR
            // Expense = 200 USD. 200 * 0.8 = 160.0 EUR
            // Balance = 4000 - 160 = 3840.0 EUR

            assertEquals(4000.0, data.totalIncome, 0.01)
            assertEquals(160.0, data.totalExpense, 0.01)
            assertEquals(3840.0, data.balance, 0.01)

            assertEquals("Good afternoon!", data.greeting)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `homeUiState emits Error when change provider exception occurs`() = runTest {

        transactionRepository.shouldHaveFilledList(true)
        budgetRepository.shouldHaveFilledList(true)
        userPreferencesRepository.setBaseCurrency("EUR")
        userPreferencesRepository.setSelectedProviderId("provider2")
        fakeExchangeRepo.setFakeProvider("provider1", "provider1")

        val viewModel = createViewModel()
        viewModel.homeUiState.test {
            awaitItem()
            val errorState = awaitItem() as HomeUiState.Error
            assertEquals(
                PROVIDER_ERROR,
                (errorState.message as UiText.DynamicString).value
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `homeUiState computes correct budget spent amounts`() = runTest {

        transactionRepository.shouldHaveFilledList(true)
        budgetRepository.shouldHaveFilledList(true)


        userPreferencesRepository.setBaseCurrency("EUR")
        userPreferencesRepository.setSelectedProviderId("provider1")
        fakeExchangeRepo.setFakeProvider("provider1", "provider1")
        fakeExchangeRepo.setFakeRate("DZD", "EUR", BigDecimal("0.8"))

        val viewModel = createViewModel()
        viewModel.homeUiState.test {
            awaitItem() // Loading
            val successState = awaitItem() as HomeUiState.Success
            val budgets = successState.data.budgets

            // Transaction 2 has amount 200.0 DZD and budgetId "1"
            // Converted amount = 200.0 * 0.8 = 160.0 EUR
            // Budget 1 limit = 500.0 DZD * 0.8 = 400.0 EUR

            val budget1 = budgets.find { it.id == "1" }
            assertEquals(160.0, budget1?.spent ?: 0.0, 0.01)
            assertEquals(400.0, budget1?.amount ?: 0.0, 0.01)
            assertEquals(240.0, budget1?.remaining ?: 0.0 , 0.01)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `homeUiState safely loads empty data with zero totals`() = runTest {
        transactionRepository.shouldHaveFilledList(false)
        budgetRepository.shouldHaveFilledList(false)

        val viewModel = createViewModel()
        viewModel.homeUiState.test {
            awaitItem() // Loading
            val successState = awaitItem() as HomeUiState.Success
            val data = successState.data

            assertEquals(0.0, data.totalIncome, 0.01)
            assertEquals(0.0, data.totalExpense, 0.01)
            assertEquals(0.0, data.balance, 0.01)
            assertEquals(0, data.totalTransactions)
            assertEquals(0.0, data.dailyAverage, 0.01)
            assertTrue(data.budgets.isEmpty())
            assertTrue(data.transactions.isEmpty())

            cancelAndIgnoreRemainingEvents()
        }
    }
}
































