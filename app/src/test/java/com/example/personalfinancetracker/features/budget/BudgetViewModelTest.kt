package com.example.personalfinancetracker.features.budget

import app.cash.turbine.test
import com.example.core.common.UiText
import com.example.domain.usecase.budget_usecases.GetBudgetTransactionsUseCase
import com.example.domain.usecase.budget_usecases.GetBudgetsUseCase
import com.example.personalfinancetracker.MainDispatcherRule
import com.example.personalfinancetracker.fakes.FakeBudgetRepository
import com.example.personalfinancetracker.fakes.FakeTransactionRepository
import com.example.personalfinancetracker.features.budget.budgets.BudgetViewModel
import com.example.personalfinancetracker.features.budget.budgets.BudgetsEvent
import com.example.personalfinancetracker.features.budget.budgets.BudgetsSideEffect
import com.example.personalfinancetracker.features.budget.budgets.BudgetsUiState
import com.example.personalfinancetracker.features.budget.model.BudgetUi
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BudgetViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val dispatcherRule = MainDispatcherRule(testDispatcher)

    private lateinit var getBudgetsUseCase: GetBudgetsUseCase
    private lateinit var getBudgetTransactionsUseCase: GetBudgetTransactionsUseCase
    private lateinit var fakeBudgetRepository: FakeBudgetRepository
    private lateinit var fakeTransactionRepository: FakeTransactionRepository

    @Before
    fun setup() {
        fakeBudgetRepository = FakeBudgetRepository()
        fakeTransactionRepository = FakeTransactionRepository()
        getBudgetsUseCase = GetBudgetsUseCase(fakeBudgetRepository)
        getBudgetTransactionsUseCase = GetBudgetTransactionsUseCase(fakeTransactionRepository)
    }

    private fun createViewModel() = BudgetViewModel(getBudgetsUseCase, getBudgetTransactionsUseCase)

    @Test
    fun `BudgetsUiState emits Loading initially`() = runTest {
        val viewModel = createViewModel()
        viewModel.budgetsUiState.test {
            val currentState = awaitItem()
            assertTrue(currentState is BudgetsUiState.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `BudgetsUiState emits Success with Budgets List`() = runTest {
        fakeBudgetRepository.shouldHaveFilledList(true)
        fakeTransactionRepository.shouldHaveFilledList(true)

        val viewModel = createViewModel()
        viewModel.budgetsUiState.test {
            awaitItem() // Loading
            val successState = awaitItem() as BudgetsUiState.Success
            assertTrue(successState.budgets.isNotEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `BudgetsUiState Success maps and calculates spent amounts correctly`() = runTest {
        fakeBudgetRepository.shouldHaveFilledList(true)
        fakeTransactionRepository.shouldHaveFilledList(true)
        val viewModel = createViewModel()
        viewModel.budgetsUiState.test {
            awaitItem() // Loading
            val successState = awaitItem() as BudgetsUiState.Success

            val budget1 = successState.budgets[0]
            assertEquals(200.0, budget1.spent)

            val budget2 = successState.budgets[1]
            assertEquals(0.0, budget2.spent)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `BudgetsUiState emits Error when exception occurs`() = runTest {
        fakeBudgetRepository.setShouldThrowError(true)
        val viewModel = createViewModel()
        viewModel.budgetsUiState.test {
            awaitItem() // Loading
            val errorState = awaitItem() as BudgetsUiState.Error
            assertEquals("Test Exception", (errorState.message as UiText.DynamicString).value)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `BudgetsUiState emits Success with empty list when list is empty`() = runTest {
        fakeBudgetRepository.shouldHaveFilledList(false)
        val viewModel = createViewModel()
        viewModel.budgetsUiState.test {
            awaitItem() // Loading
            val successState = awaitItem() as BudgetsUiState.Success
            assertTrue(successState.budgets.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent OnAddBudgetClick emits NavigateToAddBudget side effect`() = runTest {
        val viewModel = createViewModel()
        viewModel.sideEffect.test {
            viewModel.onEvent(BudgetsEvent.OnAddBudgetClick)
            assertEquals(BudgetsSideEffect.NavigateToAddBudget, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent OnBudgetClick emits NavigateToBudgetDetails side effect`() = runTest {
        val viewModel = createViewModel()
        
        // Use a completely isolated dummy item to avoid coupling to the data/domain mappers
        val dummyBudgetUi = BudgetUi(
            id = "1",
            userId = "user1",
            category = "Groceries",
            amount = 500.0,
            spent = 200.0,
            currency = "DZD",
            period = "monthly",
            notes = "Test Note",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        viewModel.sideEffect.test {
            viewModel.onEvent(BudgetsEvent.OnBudgetClick(dummyBudgetUi))
            assertEquals(BudgetsSideEffect.NavigateToBudgetDetails("1"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}