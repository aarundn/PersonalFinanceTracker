package com.example.domain.repo

import com.example.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun addTransaction(transaction: Transaction)
    fun getAllTransactions(): Flow<List<Transaction>>
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransactionById(id: String)
    suspend fun getTransactionById(id: String): Transaction?
}


