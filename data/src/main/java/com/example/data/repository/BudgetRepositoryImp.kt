package com.example.data.repository

import com.example.data.local.TrackerDatabase
import com.example.data.mapping.toDomain
import com.example.data.mapping.toEntity
import com.example.domain.model.Budget
import com.example.domain.repo.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BudgetRepositoryImp(
    private val trackerDB: TrackerDatabase
) : BudgetRepository {

    override suspend fun addBudget(budget: Budget) {
        trackerDB.budgetDao().insertBudget(budget.toEntity())
    }

    override fun getAllBudgets(): Flow<List<Budget>> =
        trackerDB.budgetDao().getAllBudgets().map { it.toDomain() }

    override suspend fun updateBudget(budget: Budget) {
        trackerDB.budgetDao().updateBudget(budget.toEntity())
    }

    override suspend fun deleteBudgetById(id: String) {
        trackerDB.budgetDao().deleteBudgetById(id)
    }

    override suspend fun getBudgetById(id: String): Budget? {
        return trackerDB.budgetDao().getBudgetById(id)?.toDomain()
    }
}
