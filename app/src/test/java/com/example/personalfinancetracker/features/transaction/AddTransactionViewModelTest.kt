package com.example.personalfinancetracker.features.transaction

import app.cash.turbine.test
import com.example.core.model.DefaultCategories
import com.example.core.model.DefaultCurrencies
import com.example.domain.usecase.ValidateInputsUseCase
import com.example.domain.usecase.budget_usecases.GetBudgetTransactionsUseCase
import com.example.domain.usecase.budget_usecases.GetBudgetsByCategoryUseCase
import com.example.domain.usecase.transaction_usecases.AddTransactionUseCase
import com.example.personalfinancetracker.MainDispatcherRule
import com.example.personalfinancetracker.fakes.FakeBudgetRepository
import com.example.personalfinancetracker.fakes.FakeTransactionRepository
import com.example.personalfinancetracker.features.transaction.add_transaction.AddTransactionEvent
import com.example.personalfinancetracker.features.transaction.add_transaction.AddTransactionSideEffect
import com.example.personalfinancetracker.features.transaction.add_transaction.AddTransactionViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddTransactionViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val dispatcherRule = MainDispatcherRule(testDispatcher)

    private lateinit var addTransactionUseCase: AddTransactionUseCase
    private lateinit var validateInputsUseCase: ValidateInputsUseCase
    private lateinit var getBudgetsByCategoryUseCase: GetBudgetsByCategoryUseCase
    private lateinit var getBudgetTransactionsUseCase: GetBudgetTransactionsUseCase
    private lateinit var transactionRepository: FakeTransactionRepository
    private lateinit var budgetRepository: FakeBudgetRepository

    @Before
    fun setup() {
        transactionRepository = FakeTransactionRepository()
        budgetRepository = FakeBudgetRepository()
        addTransactionUseCase = AddTransactionUseCase(transactionRepository)
        validateInputsUseCase = ValidateInputsUseCase()
        getBudgetsByCategoryUseCase = GetBudgetsByCategoryUseCase(budgetRepository)
        getBudgetTransactionsUseCase = GetBudgetTransactionsUseCase(transactionRepository)
    }

    private fun createViewModel() = AddTransactionViewModel(
        addTransactionUseCase = addTransactionUseCase,
        validateInputsUseCase = validateInputsUseCase,
        getBudgetsByCategoryUseCase = getBudgetsByCategoryUseCase,
        getBudgetTransactionsUseCase = getBudgetTransactionsUseCase
    )

    @Test
    fun `init populates default expense categories correctly`() = runTest {
        val viewModel = createViewModel()

        val state = viewModel.uiState.value
        assertFalse(state.isIncome)
        val expectedCategories = DefaultCategories.getCategories(isIncome = false)
        assertEquals(expectedCategories, state.categories)
    }

    @Test
    fun `changing transaction type from Expense to Income resets categories and selections`() = runTest {
        val viewModel = createViewModel()

        assertFalse(viewModel.uiState.value.isIncome)

        viewModel.onEvent(AddTransactionEvent.OnTransactionTypeChanged(isIncome = true))

        val state = viewModel.uiState.value
        assertTrue(state.isIncome)
        val expectedCategories = DefaultCategories.getCategories(isIncome = true)
        assertEquals(expectedCategories, state.categories)
        assertNull(state.selectedCategory)
        assertNull(state.selectedBudget)
    }

    @Test
    fun `OnSave with valid inputs adds transaction and triggers success effects`() = runTest {
        val viewModel = createViewModel()
        val expenseCategory = DefaultCategories.getCategories(isIncome = false).first()



        viewModel.sideEffect.test {

            viewModel.onEvent(AddTransactionEvent.OnAmountChanged("150.0"))
            viewModel.onEvent(AddTransactionEvent.OnCategoryChanged(expenseCategory))
            viewModel.onEvent(AddTransactionEvent.OnCurrencyChanged(DefaultCurrencies.DZD))
            viewModel.onEvent(AddTransactionEvent.OnSave)
            
            advanceUntilIdle()

            assertTrue(awaitItem() is AddTransactionSideEffect.NavigateBack)
            val successEffect = awaitItem()
            assertTrue(successEffect is AddTransactionSideEffect.ShowSuccess)

            transactionRepository.getAllTransactions().test {
                val transactions = awaitItem()
                assertEquals(1, transactions.size)
                assertEquals(150.0, transactions[0].amount)
                assertEquals(expenseCategory.id, transactions[0].category)
                cancelAndIgnoreRemainingEvents()
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `OnSave with database failure triggers ShowError effect`() = runTest {
        transactionRepository.shouldHaveFilledList(true)
        transactionRepository.setShouldThrowError(true)
        val expenseCategory = DefaultCategories.getCategories(isIncome = false).first()
        val viewModel = createViewModel()


        viewModel.sideEffect.test {

            viewModel.onEvent(AddTransactionEvent.OnAmountChanged("150.0"))
            viewModel.onEvent(AddTransactionEvent.OnCategoryChanged(expenseCategory))
            viewModel.onEvent(AddTransactionEvent.OnCurrencyChanged(DefaultCurrencies.DZD))

            viewModel.onEvent(AddTransactionEvent.OnSave)
            advanceUntilIdle()
            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertNotNull(state.error)

            val errorEffect = awaitItem()
            assertTrue(errorEffect is AddTransactionSideEffect.ShowError)

            cancelAndIgnoreRemainingEvents()
        }
    }
}