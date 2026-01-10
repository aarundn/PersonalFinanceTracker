package com.example.data.repository

import android.util.Log
import com.example.data.mapping.toDto
import com.example.domain.model.Transaction
import com.example.domain.repo.TransactionRepository
import com.google.firebase.firestore.FirebaseFirestore

class TransactionRepositoryImp(
    private val firestore: FirebaseFirestore
) : TransactionRepository {
    override suspend fun addTransaction(transaction: Transaction) {
        val transactionDto = transaction.toDto()
        firestore.collection(TRANSACTION_COLLECTION).document()
            .collection(transactionDto.userId)
            .add(transaction.toDto()).addOnSuccessListener {
                Log.d(TRANSACTION_COLLECTION , "added successfully")
            }.addOnFailureListener {
                Log.d(TRANSACTION_COLLECTION , "adding failed")
            }
    }

    companion object {
        const val TRANSACTION_COLLECTION = "Transactions"
    }
}