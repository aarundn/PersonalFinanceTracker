package com.example.personalfinancetracker.features.budget

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.core.R
import com.example.core.common.UiText
import com.example.core.model.DefaultCategories
import com.example.core.model.DefaultCurrencies
import com.example.domain.model.BudgetPeriod
import com.example.domain.usecase.ValidateInputsUseCase
import com.example.domain.usecase.budget_usecases.DeleteBudgetUseCase
import com.example.domain.usecase.budget_usecases.GetBudgetByIdUseCase
import com.example.domain.usecase.budget_usecases.GetBudgetTransactionsUseCase
import com.example.domain.usecase.budget_usecases.UpdateBudgetUseCase
import com.example.personalfinancetracker.MainDispatcherRule
import com.example.personalfinancetracker.fakes.FakeBudgetRepository
import com.example.personalfinancetracker.fakes.FakeTransactionRepository
import com.example.personalfinancetracker.features.budget.edit_budget.EditBudgetEvent
import com.example.personalfinancetracker.features.budget.edit_budget.EditBudgetSideEffect
import com.example.personalfinancetracker.features.budget.edit_budget.EditBudgetViewModel
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
class EditBudgetViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val dispatcherRule = MainDispatcherRule(testDispatcher)

    private lateinit var getBudgetByIdUseCase: GetBudgetByIdUseCase
    private lateinit var updateBudgetUseCase: UpdateBudgetUseCase
    private lateinit var deleteBudgetUseCase: DeleteBudgetUseCase
    private lateinit var getBudgetTransactionsUseCase: GetBudgetTransactionsUseCase
    private lateinit var validateInputsUseCase: ValidateInputsUseCase
    private lateinit var budgetRepository: FakeBudgetRepository
    private lateinit var transactionRepository: FakeTransactionRepository
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setup() {
        budgetRepository = FakeBudgetRepository()
        transactionRepository = FakeTransactionRepository()
        getBudgetByIdUseCase = GetBudgetByIdUseCase(budgetRepository)
        updateBudgetUseCase = UpdateBudgetUseCase(budgetRepository)
        deleteBudgetUseCase = DeleteBudgetUseCase(budgetRepository)
        getBudgetTransactionsUseCase = GetBudgetTransactionsUseCase(transactionRepository)
        validateInputsUseCase = ValidateInputsUseCase()
        savedStateHandle = SavedStateHandle()
    }

    private fun createViewModel() = EditBudgetViewModel(
        savedStateHandle = savedStateHandle,
        getBudgetByIdUseCase = getBudgetByIdUseCase,
        updateBudgetUseCase = updateBudgetUseCase,
        deleteBudgetUseCase = deleteBudgetUseCase,
        getBudgetTransactionsUseCase = getBudgetTransactionsUseCase,
        validatorUseCase = validateInputsUseCase
    )


    // INIT / LOADING TESTS
    @Test
    fun `init emits loading true when valid budget id is provided`() = runTest {
        budgetRepository.shouldHaveFilledList(true)
        savedStateHandle[EditBudgetViewModel.BUDGET_ID_KEY] = "1"

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val initialState = awaitItem()
            assertTrue(initialState.isLoading)
            assertNull(initialState.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `init emits error state when budget id is empty`() = runTest {
        // Don't set any budget ID in SavedStateHandle
        val viewModel = createViewModel()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.error is UiText.StringResource)
        assertEquals(
            R.string.error_invalid_budget_id,
            (state.error as UiText.StringResource).resId
        )
    }

    @Test
    fun `init populates state correctly when budget is successfully loaded`() = runTest {
        budgetRepository.shouldHaveFilledList(true)
        savedStateHandle[EditBudgetViewModel.BUDGET_ID_KEY] = "1"

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertNotNull(state.budget)
        assertEquals("1", state.budget?.id)
        assertEquals("500.0", state.amountInput)
        assertEquals("DZD", state.selectedCurrency?.id)
        assertNotNull(state.selectedCategory)
        assertNotNull(state.insights)
    }

    @Test
    fun `init emits error state when budget is not found`() = runTest {
        budgetRepository.shouldHaveFilledList(true)
        savedStateHandle[EditBudgetViewModel.BUDGET_ID_KEY] = "unknown_id"

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // Loading
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertTrue(state.error is UiText.StringResource)
            assertEquals(
                R.string.error_budget_not_found,
                (state.error as UiText.StringResource).resId
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `init emits error state when budget failed to load`() = runTest {
        budgetRepository.shouldHaveFilledList(true)
        budgetRepository.setShouldThrowError(true)

        savedStateHandle[EditBudgetViewModel.BUDGET_ID_KEY] = "1"

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // Loading
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertTrue(state.error is UiText.StringResource)
            assertEquals(
                R.string.error_failed_load_budget,
                (state.error as UiText.StringResource).resId
            )
            cancelAndIgnoreRemainingEvents()
        }
    }


    // DELETE TESTS
    @Test
    fun `OnDelete shows confirmation and OnConfirmDelete triggers deletion and success effects`() =
        runTest {
            budgetRepository.shouldHaveFilledList(true)
            savedStateHandle[EditBudgetViewModel.BUDGET_ID_KEY] = "1"
            val viewModel = createViewModel()
            advanceUntilIdle()

            viewModel.sideEffect.test {
                viewModel.onEvent(EditBudgetEvent.OnDelete)
                assertTrue(viewModel.uiState.value.showDeleteConfirmation)

                viewModel.onEvent(EditBudgetEvent.OnConfirmDelete)
                advanceUntilIdle()
                assertFalse(viewModel.uiState.value.showDeleteConfirmation)

                assertTrue(awaitItem() is EditBudgetSideEffect.ShowSuccess)
                assertTrue(awaitItem() is EditBudgetSideEffect.NavigateBack)

                assertNull(budgetRepository.getBudgetById("1"))

                cancelAndIgnoreRemainingEvents()
            }
        }

    // SAVE TESTS
    @Test
    fun `OnSave with valid inputs triggers update and success side effects`() = runTest {
        budgetRepository.shouldHaveFilledList(true)
        savedStateHandle[EditBudgetViewModel.BUDGET_ID_KEY] = "1"
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.sideEffect.test {
            viewModel.onEvent(EditBudgetEvent.OnAmountChanged("999.0"))
            viewModel.onEvent(EditBudgetEvent.OnSave)

            advanceUntilIdle()

            val successEffect = awaitItem()
            assertTrue(successEffect is EditBudgetSideEffect.ShowSuccess)

            // Verify repository was updated
            val updated = budgetRepository.getBudgetById("1")
            assertEquals(999.0, updated?.amount)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `OnSave with invalid amount triggers ShowError side effect`() = runTest {
        budgetRepository.shouldHaveFilledList(true)
        savedStateHandle[EditBudgetViewModel.BUDGET_ID_KEY] = "1"
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.sideEffect.test {
            viewModel.onEvent(EditBudgetEvent.OnAmountChanged(""))
            viewModel.onEvent(EditBudgetEvent.OnSave)

            advanceUntilIdle()

            val errorEffect = awaitItem()
            assertTrue(errorEffect is EditBudgetSideEffect.ShowError)

            // Repository should NOT have been modified
            assertEquals(500.0, budgetRepository.getBudgetById("1")?.amount)

            cancelAndIgnoreRemainingEvents()
        }
    }


    // INSIGHTS CALCULATION TESTS
    @Test
    fun `insights are calculated correctly when budget has transactions`() = runTest {

        budgetRepository.shouldHaveFilledList(true)
        transactionRepository.shouldHaveFilledList(true)

        savedStateHandle[EditBudgetViewModel.BUDGET_ID_KEY] = "1"
        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.insights)
        val insights = state.insights!!


        assertEquals(0, insights.daysElapsed)
        assertEquals(200.0, insights.projectedTotal)

        // 500.0 budget >= 200.0 projected -> NOT over budget
        assertFalse(insights.isProjectedOverBudget)

        // The budget spent should reflect our transaction
        assertEquals(200.0, state.budget?.spent)
    }

    @Test
    fun `changing period recalculates insights with new days`() = runTest {
        budgetRepository.shouldHaveFilledList(true)
        transactionRepository.shouldHaveFilledList(true)

        savedStateHandle[EditBudgetViewModel.BUDGET_ID_KEY] = "1"
        val viewModel = createViewModel()
        advanceUntilIdle()

        // Initially loaded as "monthly" (30 days)
        val initialInsights = viewModel.uiState.value.insights!!
        assertEquals(30, initialInsights.daysTotal)

        // Change to Weekly (7 days)
        viewModel.onEvent(EditBudgetEvent.OnPeriodChanged(BudgetPeriod.Weekly))

        val updatedState = viewModel.uiState.value
        val updatedInsights = updatedState.insights!!

        assertEquals(7, updatedInsights.daysTotal)
        assertEquals(BudgetPeriod.Weekly, updatedState.periodInput)
    }
}