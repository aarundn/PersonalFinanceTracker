package com.example.personalfinancetracker.fakes

import com.example.domain.model.Budget
import com.example.domain.repo.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeBudgetRepository : BudgetRepository {

    private val budgetsFlow = MutableStateFlow<List<Budget>>(emptyList())
    private var shouldReturnFilledList = false

    fun shouldHaveFilledList(shouldHave: Boolean) {
        shouldReturnFilledList = shouldHave
        if (shouldHave) {
            budgetsFlow.value = listOf(
                Budget(
                    id = "1",
                    userId = "user1",
                    category = "Groceries",
                    amount = 500.0,
                    currency = "DZD",
                    period = "monthly",
                    notes = "Monthly groceries budget",
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    syncStatus = "SYNCED"
                ),
                Budget(
                    id = "2",
                    userId = "user1",
                    category = "Entertainment",
                    amount = 200.0,
                    currency = "DZD",
                    period = "monthly",
                    notes = "Fun money",
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    syncStatus = "SYNCED"
                )
            )
        } else {
            budgetsFlow.value = emptyList()
        }
    }

    override suspend fun addBudget(budget: Budget) {
        val currentList = budgetsFlow.value.toMutableList()
        currentList.add(budget)
        budgetsFlow.value = currentList
    }

    private var shouldThrowError = false

    fun setShouldThrowError(shouldThrow: Boolean) {
        shouldThrowError = shouldThrow
    }

    override fun getAllBudgets(): Flow<List<Budget>> {
        return budgetsFlow.map { list ->
            if (shouldThrowError) {
                throw Exception("Test Exception")
            }
            list
        }
    }

    override suspend fun updateBudget(budget: Budget) {
        val currentList = budgetsFlow.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == budget.id }
        if (index != -1) {
            currentList[index] = budget
            budgetsFlow.value = currentList
        }
    }

    override suspend fun deleteBudgetById(id: String) {
        val currentList = budgetsFlow.value.toMutableList()
        currentList.removeAll { it.id == id }
        budgetsFlow.value = currentList
    }

    override suspend fun getBudgetById(id: String): Budget? {
        return budgetsFlow.value.find { it.id == id }
    }

    override fun getBudgetsByCategory(category: String): Flow<List<Budget>> {
        return budgetsFlow.map { list -> list.filter { it.category == category } }
    }

    override suspend fun syncWithRemote(): Result<Unit> = Result.success(Unit)
    override suspend fun resolveBudgetsConflict(): Result<Unit> = Result.success(Unit)
}
