package com.example.data.repository

import com.example.data.local.TrackerDatabase
import com.example.data.mapping.toDomain
import com.example.data.mapping.toEntity
import com.example.domain.model.Transaction
import com.example.domain.repo.TransactionRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepositoryImp(
    private val firestore: FirebaseFirestore,
    private val transactionDB: TrackerDatabase
) : TransactionRepository {
    override suspend fun addTransaction(transaction: Transaction) {
        val transaction = transaction.toEntity()

        runCatching {
            transactionDB.transactionDao().insertTransaction(transaction)
        }

    }      /*firestore.collection(TRANSACTION_COLLECTION).document(transactionDto.userId)
            .collection(TRANSACTION_COLLECTION)
            .add(transaction.toDto()).addOnSuccessListener {
                Log.d(TRANSACTION_COLLECTION , "added successfully")
            }.addOnFailureListener {
                Log.d(TRANSACTION_COLLECTION , "adding failed ${it.message}")
            }
    }

    companion object {
        const val TRANSACTION_COLLECTION = "Transactions"
    }*/

    override fun getAllTransactions(): Flow<List<Transaction>> =
        transactionDB.transactionDao().getAllTransactions().map { it.toDomain() }

}