package com.example.data.local

import com.example.data.local.model.BudgetEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeBudgetDao: BudgetDao {

    val table = mutableListOf<BudgetEntity>()

    override fun getAllBudgets(): Flow<List<BudgetEntity>> {
        return flowOf(table)
    }

    override suspend fun insertBudget(budget: BudgetEntity): Long {
        table.add(budget)
        return table.size.toLong()
    }

    override suspend fun insertBudgets(budgets: List<BudgetEntity>) {
        table.addAll(budgets)
    }

    override suspend fun updateBudget(budget: BudgetEntity) {
        val index = table.indexOfFirst { it.id == budget.id }
        if (index != -1) {
            table[index] = budget
        }
    }

    override suspend fun deleteBudgetById(id: String) {
        table.removeIf { it.id == id }
    }

    override suspend fun getBudgetById(id: String): BudgetEntity? {
        return table.find { it.id == id }
    }

    override fun getBudgetsByCategory(category: String): Flow<List<BudgetEntity>> {
        return flowOf(table.filter { it.category == category })
    }

    override suspend fun getUnsyncedBudgets(): List<BudgetEntity> {
        return table.filter { it.syncStatus == "PENDING" }
    }

    override suspend fun updateSyncStatus(id: String, status: String) {
        val index = table.indexOfFirst { it.id == id }
        if (index != -1) {
            table[index] = table[index].copy(syncStatus = status)
        }
    }

    override suspend fun getAllBudgetsOnce(): List<BudgetEntity> {
        return table
    }

    override suspend fun getDeletedBudgets(status: String): List<BudgetEntity> {
        return table.filter { it.syncStatus == status }
    }

    override suspend fun deleteAllBudgets() {
        table.clear()
    }
}