package com.example.data.remote

import com.example.data.remote.budget.RemoteBudgetRepo
import com.example.data.remote.model.BudgetDto

class FakeRemoteBudget: RemoteBudgetRepo {
    val serverDb = mutableListOf<BudgetDto>()
    override suspend fun addBudget(budget: BudgetDto) {
        serverDb.add(budget)
    }

    override suspend fun updateBudget(budget: BudgetDto) {
        val index = serverDb.indexOfFirst { it.id == budget.id }
        if (index != -1) {
            serverDb[index] = budget
        }
    }

    override suspend fun deleteBudgetById(id: String) {
        serverDb.removeIf { it.id == id }
    }

    override suspend fun getAllBudgetsOnce(): List<BudgetDto> {
        return serverDb
    }
}