package com.example.personalfinancetracker.features.transaction

import app.cash.turbine.test
import com.example.core.common.UiText
import com.example.domain.model.Type
import com.example.domain.usecase.transaction_usecases.GetTransactionsUseCase
import com.example.personalfinancetracker.MainDispatcherRule
import com.example.personalfinancetracker.fakes.FakeTransactionRepository
import com.example.personalfinancetracker.features.transaction.model.TransactionUi
import com.example.personalfinancetracker.features.transaction.transactions.TransactionsEvent
import com.example.personalfinancetracker.features.transaction.transactions.TransactionsSideEffect
import com.example.personalfinancetracker.features.transaction.transactions.TransactionsUiState
import com.example.personalfinancetracker.features.transaction.transactions.TransactionsViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TransactionsViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val dispatcherRule = MainDispatcherRule(testDispatcher)

    private lateinit var getTransactionsUseCase: GetTransactionsUseCase
    private lateinit var fakeTransactionRepository: FakeTransactionRepository

    @Before
    fun setup() {
        fakeTransactionRepository = FakeTransactionRepository()
        getTransactionsUseCase = GetTransactionsUseCase(fakeTransactionRepository)
    }

    private fun createViewModel() = TransactionsViewModel(getTransactionsUseCase)

    @Test
    fun `TransactionsUiState emits Loading initially`() = runTest {
        val viewModel = createViewModel()
        viewModel.transactionsUiState.test {
            val currentState = awaitItem()
            assertTrue(currentState is TransactionsUiState.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `TransactionsUiState emits Success with Transactions List`() = runTest {
        fakeTransactionRepository.shouldHaveFilledList(true)

        val viewModel = createViewModel()
        viewModel.transactionsUiState.test {
            awaitItem() // Loading
            val successState = awaitItem() as TransactionsUiState.Success
            assertTrue(successState.transactions.isNotEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `TransactionsUiState emits Error when exception occurs`() = runTest {
        fakeTransactionRepository.setShouldThrowError(true)
        val viewModel = createViewModel()
        viewModel.transactionsUiState.test {
            awaitItem() // Loading
            val errorState = awaitItem() as TransactionsUiState.Error
            assertEquals("Test Exception", (errorState.message as UiText.DynamicString).value)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `TransactionsUiState emits Success with empty list when list is empty`() = runTest {
        fakeTransactionRepository.shouldHaveFilledList(false)
        val viewModel = createViewModel()
        viewModel.transactionsUiState.test {
            awaitItem() // Loading
            val successState = awaitItem() as TransactionsUiState.Success
            assertTrue(successState.transactions.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent OnAddTransactionClick emits NavigateToAddTransaction side effect`() = runTest {
        val viewModel = createViewModel()

        viewModel.sideEffect.test {
            viewModel.onEvent(TransactionsEvent.OnAddTransactionClick)
            assertEquals(TransactionsSideEffect.NavigateToAddTransaction, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
    @Test
    fun `onEvent OnTransactionClick emits NavigateToTransactionDetails side effect`() = runTest {
        val viewModel = createViewModel()

        val dummyTransaction = TransactionUi(
            id = "1",
            userId = "user1",
            amount = 5000.0,
            currency = "DZD",
            categoryId = "salary",
            date = System.currentTimeMillis(),
            notes = "Monthly salary",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            type = Type.INCOME,
            budgetId = "budget1",

        )
        viewModel.sideEffect.test {
            viewModel.onEvent(TransactionsEvent.OnTransactionClick(dummyTransaction))
            assertEquals(TransactionsSideEffect.NavigateToTransactionDetails("1"),
                awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}