package com.example.data.remote.budget

import com.example.data.remote.model.BudgetDto
import kotlinx.coroutines.flow.Flow

interface RemoteBudgetRepo {
    suspend fun addBudget(budget: BudgetDto)
    fun getAllBudgets(): Flow<List<BudgetDto>>
    suspend fun updateBudget(budget: BudgetDto)
    suspend fun deleteBudgetById(id: String)
    suspend fun getAllBudgetsOnce(): List<BudgetDto>
}
