package com.example.data.remote.budget

import com.example.data.remote.model.BudgetDto

interface RemoteBudgetRepo {
    suspend fun addBudget(budget: BudgetDto)
    suspend fun updateBudget(budget: BudgetDto)
    suspend fun deleteBudgetById(id: String)
    suspend fun getAllBudgetsOnce(): List<BudgetDto>
}
