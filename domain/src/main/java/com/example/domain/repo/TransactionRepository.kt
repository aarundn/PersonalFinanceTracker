package com.example.domain.repo

import com.example.domain.model.Transaction

interface TransactionRepository {
    suspend fun addTransaction(transaction: Transaction)
}