package com.example.personalfinancetracker.features.transaction

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.core.R
import com.example.core.common.UiText
import com.example.domain.usecase.ValidateInputsUseCase
import com.example.domain.usecase.budget_usecases.GetBudgetTransactionsUseCase
import com.example.domain.usecase.budget_usecases.GetBudgetsByCategoryUseCase
import com.example.domain.usecase.transaction_usecases.DeleteTransactionUseCase
import com.example.domain.usecase.transaction_usecases.GetTransactionByIdUseCase
import com.example.domain.usecase.transaction_usecases.UpdateTransactionUseCase
import com.example.personalfinancetracker.MainDispatcherRule
import com.example.personalfinancetracker.fakes.FakeBudgetRepository
import com.example.personalfinancetracker.fakes.FakeTransactionRepository
import com.example.personalfinancetracker.features.transaction.edit_transaction.EditTransactionEvent
import com.example.personalfinancetracker.features.transaction.edit_transaction.EditTransactionSideEffect
import com.example.personalfinancetracker.features.transaction.edit_transaction.EditTransactionViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
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
class EditTransactionViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val dispatcherRule = MainDispatcherRule(testDispatcher)

    private lateinit var getTransactionByIdUseCase: GetTransactionByIdUseCase
    private lateinit var updateTransactionUseCase: UpdateTransactionUseCase
    private lateinit var transactionRepository: FakeTransactionRepository
    private lateinit var budgetRepository: FakeBudgetRepository
    private lateinit var deleteTransactionUseCase: DeleteTransactionUseCase
    private lateinit var getBudgetTransactionsUseCase: GetBudgetTransactionsUseCase
    private lateinit var validateInputsUseCase: ValidateInputsUseCase
    private lateinit var getBudgetsByCategoryUseCase: GetBudgetsByCategoryUseCase
    private lateinit var savedStateHandle: SavedStateHandle


    @Before
    fun setup() {
        transactionRepository = FakeTransactionRepository()
        budgetRepository = FakeBudgetRepository()
        getTransactionByIdUseCase = GetTransactionByIdUseCase(transactionRepository)
        updateTransactionUseCase = UpdateTransactionUseCase(transactionRepository)
        deleteTransactionUseCase = DeleteTransactionUseCase(transactionRepository)
        getBudgetTransactionsUseCase = GetBudgetTransactionsUseCase(transactionRepository)
        validateInputsUseCase = ValidateInputsUseCase()
        getBudgetsByCategoryUseCase = GetBudgetsByCategoryUseCase(budgetRepository)
        savedStateHandle = SavedStateHandle()
    }

    private fun createViewModel() = EditTransactionViewModel(
        savedStateHandle = savedStateHandle,
        getTransactionByIdUseCase = getTransactionByIdUseCase,
        updateTransactionUseCase = updateTransactionUseCase,
        deleteTransactionUseCase = deleteTransactionUseCase,
        validateInputsUseCase = validateInputsUseCase,
        getBudgetsByCategoryUseCase = getBudgetsByCategoryUseCase,
        getBudgetTransactionsUseCase = getBudgetTransactionsUseCase
    )


    @Test
    fun `init emits loading true when valid transaction id is provided`() = runTest {

        savedStateHandle[EditTransactionViewModel.TRANSACTION_ID_KEY] = "tx_1"
        val viewModel = createViewModel()

        viewModel.uiState.test {

            val initialState = awaitItem()
            println("Initial state: $initialState")
            assertTrue(initialState.isLoading)
            assertNull(initialState.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `init emits error state when transaction id is empty`() = runTest {

        val viewModel = createViewModel()
        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertTrue(state.error is UiText.StringResource)
        assertEquals(
            R.string.error_invalid_transaction_id,
            (state.error as UiText.StringResource).resId
        )
    }

    @Test
    fun `init populates state correctly when transaction is successfully loaded`() = runTest {
        transactionRepository.shouldHaveFilledList(true)
        budgetRepository.shouldHaveFilledList(true)
        savedStateHandle[EditTransactionViewModel.TRANSACTION_ID_KEY] = "2"

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val initialState = awaitItem() //Loading
            println("Initial state: $initialState")
            val state = awaitItem()
            println("State: $state")
            assertFalse(state.isLoading)
            assertEquals("2", state.transaction?.id)
            assertEquals("food", state.selectedCategory?.id)
            assertEquals("1", state.transaction?.budgetId)
            assertTrue(!state.isIncome)
            assertEquals("200.0", state.amount)
            assertEquals("DZD", state.selectedCurrency?.id)
            assertNull(state.error)
            println("State: ${awaitItem()}")
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `init populates state correctly when budgets is successfully loaded with transaction category`() =
        runTest {
            transactionRepository.shouldHaveFilledList(true)
            budgetRepository.shouldHaveFilledList(true)
            savedStateHandle[EditTransactionViewModel.TRANSACTION_ID_KEY] = "2"

            val viewModel = createViewModel()
            advanceUntilIdle()
            viewModel.uiState.test {
                val state = awaitItem()
                assertFalse(state.isLoading)
                assertTrue(state.availableBudgets.isNotEmpty())
                assertEquals("1", state.selectedBudget?.id)
                assertTrue(!state.isIncome)
            }
        }

    @Test
    fun `init emits error state when transaction is not found`() = runTest {
        transactionRepository.shouldHaveFilledList(true)
        savedStateHandle[EditTransactionViewModel.TRANSACTION_ID_KEY] = "unknown_id"

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // Initial Loading
            val state = awaitItem()
            println("State: ${state.error}")
            assertFalse(state.isLoading)
            assertTrue(state.error is UiText.StringResource)
            assertEquals(
                R.string.error_transaction_not_found,
                (state.error as UiText.StringResource).resId
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `init emits error state when transaction failed to load`() = runTest {
        transactionRepository.shouldHaveFilledList(true)
        transactionRepository.setShouldThrowError(true)

        savedStateHandle[EditTransactionViewModel.TRANSACTION_ID_KEY] = "1"

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // Initial Loading

            val state = awaitItem()
            assertFalse(state.isLoading)
            assertTrue(state.error is UiText.StringResource)
            assertEquals(
                R.string.error_failed_load_transaction,
                (state.error as UiText.StringResource).resId
            )

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `OnDelete shows confirmation and OnConfirmDelete triggers deletion and success side effects`() =
        runTest {
            transactionRepository.shouldHaveFilledList(true)
            savedStateHandle[EditTransactionViewModel.TRANSACTION_ID_KEY] = "1"
            val viewModel = createViewModel()
            advanceUntilIdle()
            viewModel.sideEffect.test {

                viewModel.onEvent(EditTransactionEvent.OnDelete)
                assertTrue(viewModel.uiState.value.showDeleteConfirmation)

                viewModel.onEvent(EditTransactionEvent.OnConfirmDelete)
                advanceUntilIdle()
                assertFalse(viewModel.uiState.value.showDeleteConfirmation)

                assertTrue(awaitItem() is EditTransactionSideEffect.ShowSuccess)

                assertTrue(awaitItem() is EditTransactionSideEffect.NavigateBack)
                assertNull(transactionRepository.getTransactionById("1"))

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `OnSave with valid inputs triggers update and success side effects`() = runTest {
        transactionRepository.shouldHaveFilledList(true)
        savedStateHandle[EditTransactionViewModel.TRANSACTION_ID_KEY] = "1"
        val viewModel = createViewModel()
        advanceUntilIdle()
        viewModel.sideEffect.test {

            viewModel.onEvent(EditTransactionEvent.OnAmountChanged("900.0"))
            viewModel.onEvent(EditTransactionEvent.OnSave)

            advanceUntilIdle()
            val successEffect = awaitItem()
            assertTrue(successEffect is EditTransactionSideEffect.ShowSuccess)

            val navEffect = awaitItem()
            assertTrue(navEffect is EditTransactionSideEffect.NavigateBack)

            assertEquals(900.0, transactionRepository.getTransactionById("1")?.amount)

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `OnSave with invalid amount triggers ShowError side effect`() = runTest {
        transactionRepository.shouldHaveFilledList(true)
        savedStateHandle[EditTransactionViewModel.TRANSACTION_ID_KEY] = "1"
        val viewModel = createViewModel()
        advanceUntilIdle()
        viewModel.sideEffect.test {
            viewModel.onEvent(EditTransactionEvent.OnAmountChanged(""))
            viewModel.onEvent(EditTransactionEvent.OnSave)

            advanceUntilIdle()
            val errorEffect = awaitItem()
            assertTrue(errorEffect is EditTransactionSideEffect.ShowError)

            assertEquals(5000.0, transactionRepository.getTransactionById("1")?.amount)

            cancelAndIgnoreRemainingEvents()
        }
    }
}

































