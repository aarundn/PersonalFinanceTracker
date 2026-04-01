package com.example.data.remote.transaction

import com.example.data.remote.model.TransactionsDto
import kotlinx.coroutines.flow.Flow

interface RemoteTransactionRepo {
    suspend fun addTransaction(transaction: TransactionsDto)
    fun getAllTransactions(): Flow<List<TransactionsDto>>
    suspend fun updateTransaction(transaction: TransactionsDto)
    suspend fun deleteTransactionById(id: String)
    fun getTransactionsByBudgetId(budgetId: String): Flow<List<TransactionsDto>>
    suspend fun updateTransactionsWithResolvedData(resolvedTransactions: List<TransactionsDto>)
    suspend fun fetchAndSyncRemoteTransactions()
}