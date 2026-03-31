package com.example.data.remote.budget

import com.example.data.remote.model.BudgetDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class RemoteBudgetRepoImpl(
    private val firestore: FirebaseFirestore
) : RemoteBudgetRepo {
    override suspend fun addBudget(budget: BudgetDto) {
        firestore.collection("budgets").document(budget.id).set(budget)
    }

    override fun getAllBudgets(): Flow<List<BudgetDto>> =
        firestore.collection("budgets")
            .snapshots()
            .catch { throw it }
            .map { snapshot ->
                snapshot.toObjects(BudgetDto::class.java)
            }

    override suspend fun updateBudget(budget: BudgetDto) {
        firestore.collection("budgets").document(budget.id).set(budget)
    }

    override suspend fun deleteBudgetById(id: String) {
        firestore.collection("budgets").document(id).delete()
    }
}
