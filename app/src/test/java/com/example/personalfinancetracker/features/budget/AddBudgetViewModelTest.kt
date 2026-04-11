package com.example.personalfinancetracker.features.budget

import app.cash.turbine.test
import com.example.core.R
import com.example.core.model.DefaultCategories
import com.example.core.model.DefaultCurrencies
import com.example.domain.usecase.ValidateInputsUseCase
import com.example.domain.usecase.budget_usecases.AddBudgetUseCase
import com.example.personalfinancetracker.MainDispatcherRule
import com.example.personalfinancetracker.fakes.FakeBudgetRepository
import com.example.personalfinancetracker.features.budget.add_budget.AddBudgetEvent
import com.example.personalfinancetracker.features.budget.add_budget.AddBudgetSideEffect
import com.example.personalfinancetracker.features.budget.add_budget.AddBudgetViewModel
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
class AddBudgetViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val dispatcherRule = MainDispatcherRule(testDispatcher)

    private lateinit var addBudgetUseCase: AddBudgetUseCase
    private lateinit var validateInputsUseCase: ValidateInputsUseCase
    private lateinit var budgetRepository: FakeBudgetRepository

    @Before
    fun setup() {
        budgetRepository = FakeBudgetRepository()
        addBudgetUseCase = AddBudgetUseCase(budgetRepository)
        validateInputsUseCase = ValidateInputsUseCase()
    }

    private fun createViewModel() = AddBudgetViewModel(
        addBudgetUseCase = addBudgetUseCase,
        validatorUseCase = validateInputsUseCase
    )

    @Test
    fun `init populates default expense categories correctly`() = runTest {
        val viewModel = createViewModel()

        val state = viewModel.uiState.value
        val expectedCategories = DefaultCategories.getCategories(isIncome = false)
        assertEquals(expectedCategories, state.categories)
        assertNull(state.selectedCategory)
        assertNull(state.selectedCurrency)
        assertEquals("", state.amountInput)
    }

    @Test
    fun `OnSave with valid inputs adds budget and triggers success effects`() = runTest {
        val viewModel = createViewModel()
        val expenseCategory = DefaultCategories.getCategories(isIncome = false).first()

        viewModel.sideEffect.test {
            viewModel.onEvent(AddBudgetEvent.OnAmountChanged("500.0"))
            viewModel.onEvent(AddBudgetEvent.OnCategorySelected(expenseCategory))
            viewModel.onEvent(AddBudgetEvent.OnCurrencyChanged(DefaultCurrencies.DZD))
            viewModel.onEvent(AddBudgetEvent.OnSave)

            advanceUntilIdle()

            val successEffect = awaitItem()
            assertTrue(successEffect is AddBudgetSideEffect.ShowSuccess)

            assertTrue(awaitItem() is AddBudgetSideEffect.NavigateBack)

            // Verify repository has the budget
            budgetRepository.getAllBudgets().test {
                val budgets = awaitItem()
                assertEquals(1, budgets.size)
                assertEquals(500.0, budgets[0].amount)
                assertEquals(expenseCategory.id, budgets[0].category)
                assertEquals("DZD", budgets[0].currency)
                cancelAndIgnoreRemainingEvents()
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `OnSave with invalid amount triggers ShowError side effect`() = runTest {
        val viewModel = createViewModel()
        val expenseCategory = DefaultCategories.getCategories(isIncome = false).first()

        viewModel.sideEffect.test {
            viewModel.onEvent(AddBudgetEvent.OnCategorySelected(expenseCategory))
            viewModel.onEvent(AddBudgetEvent.OnCurrencyChanged(DefaultCurrencies.DZD))
            viewModel.onEvent(AddBudgetEvent.OnSave)

            advanceUntilIdle()

            val errorEffect = awaitItem()
            assertTrue(errorEffect is AddBudgetSideEffect.ShowError)
            assertFalse(viewModel.uiState.value.isSaving)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `OnSave with database failure triggers ShowError effect`() = runTest {
        budgetRepository.setShouldThrowError(true)
        val viewModel = createViewModel()
        val expenseCategory = DefaultCategories.getCategories(isIncome = false).first()

        viewModel.sideEffect.test {
            viewModel.onEvent(AddBudgetEvent.OnAmountChanged("500.0"))
            viewModel.onEvent(AddBudgetEvent.OnCategorySelected(expenseCategory))
            viewModel.onEvent(AddBudgetEvent.OnCurrencyChanged(DefaultCurrencies.DZD))
            viewModel.onEvent(AddBudgetEvent.OnSave)

            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertFalse(state.isSaving)
            assertNotNull(state.error)
            assertTrue(state.error is com.example.core.common.UiText.StringResource)
            assertEquals(
                R.string.error_failed_save_budget,
                (state.error as com.example.core.common.UiText.StringResource).resId
            )

            val errorEffect = awaitItem()
            assertTrue(errorEffect is AddBudgetSideEffect.ShowError)

            cancelAndIgnoreRemainingEvents()
        }
    }
}