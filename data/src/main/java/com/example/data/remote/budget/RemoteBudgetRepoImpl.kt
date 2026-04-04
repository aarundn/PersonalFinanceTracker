package com.example.data.remote.budget

import com.example.data.remote.model.BudgetDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RemoteBudgetRepoImpl(
    private val firestore: FirebaseFirestore
) : RemoteBudgetRepo {
    override suspend fun addBudget(budget: BudgetDto) {
        firestore.collection(COLLECTION_NAME).document(budget.id).set(budget)
    }
    override suspend fun updateBudget(budget: BudgetDto) {
        firestore.collection(COLLECTION_NAME).document(budget.id).set(budget)
    }

    override suspend fun deleteBudgetById(id: String) {
        firestore.collection(COLLECTION_NAME).document(id).delete()
    }

    override suspend fun getAllBudgetsOnce(): List<BudgetDto> =
        firestore.collection(COLLECTION_NAME)
            .get()
            .await()
            .toObjects(BudgetDto::class.java)

    companion object
    {
        const val COLLECTION_NAME = "budgets"
    }
}
