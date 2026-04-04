package com.example.data.remote.transaction

import com.example.data.remote.model.TransactionsDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class RemoteTransactionRepoImpl(
    private val firestore: FirebaseFirestore,
) : RemoteTransactionRepo {
    override suspend fun addTransaction(transaction: TransactionsDto) {
        firestore.collection(COLLECTION_NAME)
            .document(transaction.id)
            .set(transaction)
            .await()
    }

    override suspend fun getAllTransactions(): List<TransactionsDto> =
        firestore.collection(COLLECTION_NAME)
            .get().await().map { it.toObject(TransactionsDto::class.java) }

    override suspend fun updateTransaction(transaction: TransactionsDto) {
        firestore.collection(COLLECTION_NAME)
            .document(transaction.id)
            .set(transaction)
            .await()
    }

    override suspend fun deleteTransactionById(id: String) {
        firestore.collection(COLLECTION_NAME).document(id)
            .delete()
            .await()
    }

    override fun getTransactionsByBudgetId(budgetId: String): Flow<List<TransactionsDto>> =
        firestore.collection(COLLECTION_NAME)
            .whereEqualTo("budget_id", budgetId)
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(TransactionsDto::class.java)
            }


    companion object
    {
        const val COLLECTION_NAME = "transactions"
    }
}
