package com.example.data.local

import com.example.data.local.model.TransactionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeTransactionDao : TransactionDao {
    private val transactions = mutableListOf<TransactionEntity>()

    override fun getAllTransactions(): Flow<List<TransactionEntity>> {
        return flowOf(transactions)
    }

    override fun getAllTransactionsOnce(): List<TransactionEntity> {
        return transactions
    }

    override fun getTransactionsByCategory(category: String): Flow<List<TransactionEntity>> {
        return flowOf(transactions.filter { it.category == category })
    }

    override suspend fun insertTransaction(transaction: TransactionEntity): Long {
        transactions.add(transaction)
        return transactions.size.toLong()
    }

    override suspend fun insertTransactions(transactions: List<TransactionEntity>) {
        this.transactions.addAll(transactions)
    }

    override suspend fun upsertTransaction(transaction: List<TransactionEntity>) {

    }

    override suspend fun updateTransaction(transaction: TransactionEntity) {
        val index = transactions.indexOfFirst { it.id == transaction.id }
        if (index != -1) {
            transactions[index] = transaction
        }
    }

    override suspend fun deleteTransactionById(id: String) {
        transactions.removeIf { it.id == id }
    }

    override suspend fun deleteAllTransactions() {
        transactions.clear()
    }

    override suspend fun getTransactionById(id: String): TransactionEntity? {
        return transactions.find { it.id == id }
    }

    override fun getTransactionsByBudgetId(budgetId: String): Flow<List<TransactionEntity>> {
        return flowOf(transactions.filter { it.budgetId == budgetId })
    }

    override suspend fun getUnsyncedTransactions(): List<TransactionEntity> {
        return transactions.filter { it.syncStatus == "PENDING" }
    }

    override suspend fun updateSyncStatus(id: String, status: String) {
        val index = transactions.indexOfFirst { it.id == id }
        if (index != -1) {
            transactions[index] = transactions[index].copy(syncStatus = status)
        }
    }

    override suspend fun getDeletedTransactions(status: String): List<TransactionEntity> {
        return transactions.filter { it.syncStatus == status }
    }
}