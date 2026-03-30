package com.example.data.remote.transaction

import com.example.data.remote.model.TransactionsDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class RemoteTransactionRepoImpl(
    private val firestore: FirebaseFirestore
) : RemoteTransactionRepo {
    override suspend fun addTransaction(transaction: TransactionsDto) {
        firestore.collection("transactions").add(transaction)
    }

    override fun getAllTransactions(): Flow<List<TransactionsDto>> =
        firestore.collection("transactions")
            .snapshots()
            .catch { throw it }
            .map { snapshot ->
                snapshot.toObjects(TransactionsDto::class.java)
            }

    override suspend fun updateTransaction(transaction: TransactionsDto) {
        firestore.collection("transactions").document(transaction.id).set(transaction)
    }

    override suspend fun deleteTransactionById(id: String) {
        firestore.collection("transactions").document(id).delete()
    }

    override fun getTransactionsByBudgetId(budgetId: String): Flow<List<TransactionsDto>> =
        firestore.collection("transactions")
            .whereEqualTo("budgetId", budgetId)
            .snapshots()
            .catch { throw it }
            .map { snapshot ->
                snapshot.toObjects(TransactionsDto::class.java)
            }

}