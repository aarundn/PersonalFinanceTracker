package com.example.data.repository

import android.util.Log
import com.example.data.local.TrackerDatabase
import com.example.data.mapping.toCategoryDomain
import com.example.data.mapping.toDomain
import com.example.data.mapping.toEntity
import com.example.domain.model.Category
import com.example.domain.model.Transaction
import com.example.domain.repo.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepositoryImp(
    private val transactionDB: TrackerDatabase
) : TransactionRepository {

    override fun getAllCategories(): Flow<List<Category>> =
        transactionDB.categoryDao().getAllCategories().map { it.toCategoryDomain() }


    override suspend fun addTransaction(transaction: Transaction) {
        val transactionEntity = transaction.toEntity()

            transactionDB.transactionDao().insertTransaction(transactionEntity)
            Log.d("TransactionRepositoryImp", "Transaction added successfully")


    }

    override fun getAllTransactions(): Flow<List<Transaction>> =
        transactionDB.transactionDao().getAllTransactions().map { it.toDomain() }

    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDB.transactionDao().updateTransaction(transaction.toEntity())
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        transactionDB.transactionDao().deleteTransaction(transaction.toEntity())
    }

    override suspend fun getTransactionById(id: String): Transaction? {
        return transactionDB.transactionDao().getTransactionById(id)?.toDomain()
    }


}