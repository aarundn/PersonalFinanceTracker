package com.example.data.remote.repo

import com.example.domain.model.Transaction
import com.example.domain.repo.TransactionRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TransactionRepositoryImp(
    private val firestore: FirebaseFirestore
) : TransactionRepository {
    override suspend fun addTransaction(transaction: Transaction) {
        firestore.collection("Transactions").add(transaction).await()


    }
}