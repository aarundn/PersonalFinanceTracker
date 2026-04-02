package com.example.data.remote.transaction

import com.example.data.local.TrackerDatabase
import com.example.data.mapping.toEntity
import com.example.data.mapping.toLocal
import com.example.data.remote.model.TransactionsDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class RemoteTransactionRepoImpl(
    private val firestore: FirebaseFirestore,
    private val trackerDatabase: TrackerDatabase
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

    override suspend fun updateTransactionsWithResolvedData(resolvedTransactions: List<TransactionsDto>) {
        runCatching {
            try {
                trackerDatabase.transactionDao().deleteAllTransactions()
                println("Repository: Deleted all local transactions")
                resolvedTransactions.forEach { transaction ->
                    trackerDatabase.transactionDao().insertTransaction(transaction.toEntity())
                }

                println("Repository: Updated local database with ${resolvedTransactions.size} resolved Transactions")
            } catch (e: Exception) {
                println("Repository: Failed to update messages with resolved data: ${e.message}")
            }
        }
    }

    override suspend fun fetchAndSyncRemoteTransactions() {
        try {
            val remoteTransactions = getAllTransactions()
            val transactionEntity = remoteTransactions.toLocal()
            trackerDatabase.transactionDao().upsertTransaction(transactionEntity)
            println("Repository: Fetched and synced $remoteTransactions remote transactions")
        } catch (e: Exception) {
            println("Repository: Failed to fetch remote transactions: ${e.message}")
        }
    }

    companion object
    {
        const val COLLECTION_NAME = "transactions"
    }
}
