package com.example.domain.repo

import com.example.domain.model.Budget
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    suspend fun addBudget(budget: Budget)
    fun getAllBudgets(): Flow<List<Budget>>
    suspend fun updateBudget(budget: Budget)
    suspend fun deleteBudgetById(id: String)
    suspend fun getBudgetById(id: String): Budget?
}
