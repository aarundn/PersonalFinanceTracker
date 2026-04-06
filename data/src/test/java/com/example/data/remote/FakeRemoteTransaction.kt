package com.example.data.remote

import com.example.data.remote.model.TransactionsDto
import com.example.data.remote.transaction.RemoteTransactionRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRemoteTransaction: RemoteTransactionRepo {
    val serverDb = mutableListOf<TransactionsDto>()
    override suspend fun addTransaction(transaction: TransactionsDto) {
        serverDb.add(transaction)
    }

    override suspend fun getAllTransactions(): List<TransactionsDto> {
        return serverDb
    }

    override suspend fun updateTransaction(transaction: TransactionsDto) {
        val index = serverDb.indexOfFirst { it.id == transaction.id }
        if (index != -1) {
            serverDb[index] = transaction
        }
    }

    override suspend fun deleteTransactionById(id: String) {
        serverDb.removeIf { it.id == id }
    }

    override fun getTransactionsByBudgetId(budgetId: String): Flow<List<TransactionsDto>> {
        return flowOf(serverDb.filter { it.budgetId == budgetId })
    }
}