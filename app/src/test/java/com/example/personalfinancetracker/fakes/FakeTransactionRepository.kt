package com.example.personalfinancetracker.fakes

import com.example.domain.model.Transaction
import com.example.domain.model.Type
import com.example.domain.repo.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeTransactionRepository : TransactionRepository {

    private val transactionsFlow = MutableStateFlow<List<Transaction>>(emptyList())
    private var shouldReturnFilledList = false

    fun shouldHaveFilledList(shouldHave: Boolean) {
        shouldReturnFilledList = shouldHave
        if (shouldHave) {
            transactionsFlow.value = listOf(
                Transaction(
                    id = "1",
                    userId = "user1",
                    amount = 5000.0,
                    currency = "DZD",
                    category = "Salary",
                    date = System.currentTimeMillis(),
                    notes = "Monthly salary",
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    type = Type.INCOME,
                    budgetId = null,
                    syncStatus = "SYNCED"
                ),
                Transaction(
                    id = "2",
                    userId = "user1",
                    amount = 200.0,
                    currency = "DZD",
                    category = "Groceries",
                    date = System.currentTimeMillis(),
                    notes = "Weekly groceries",
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    type = Type.EXPENSE,
                    budgetId = "1",
                    syncStatus = "SYNCED"
                )
            )
        } else {
            transactionsFlow.value = emptyList()
        }
    }

    override suspend fun addTransaction(transaction: Transaction) {
        val currentList = transactionsFlow.value.toMutableList()
        currentList.add(transaction)
        transactionsFlow.value = currentList
    }

    private var shouldThrowError = false

    fun setShouldThrowError(shouldThrow: Boolean) {
        shouldThrowError = shouldThrow
    }

    override fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionsFlow.map { list ->
            if (shouldThrowError) {
                throw Exception("Test Exception")
            }
            list
        }
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        val currentList = transactionsFlow.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == transaction.id }
        if (index != -1) {
            currentList[index] = transaction
            transactionsFlow.value = currentList
        }
    }

    override suspend fun deleteTransactionById(id: String) {
        val currentList = transactionsFlow.value.toMutableList()
        currentList.removeAll { it.id == id }
        transactionsFlow.value = currentList
    }

    override suspend fun getTransactionById(id: String): Transaction? {
        return transactionsFlow.value.find { it.id == id }
    }

    override fun getTransactionsByBudgetId(budgetId: String): Flow<List<Transaction>> {
        return transactionsFlow.map { list -> list.filter { it.budgetId == budgetId } }
    }

    override suspend fun syncWithRemote(): Result<Unit> = Result.success(Unit)
    override suspend fun resolveTransactionsConflict(): Result<Unit> = Result.success(Unit)
}
