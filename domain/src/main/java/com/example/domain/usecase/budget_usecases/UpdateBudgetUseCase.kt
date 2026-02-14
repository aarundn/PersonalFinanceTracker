package com.example.domain.usecase.budget_usecases

import com.example.domain.model.Budget
import com.example.domain.repo.BudgetRepository

class UpdateBudgetUseCase(
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(budget: Budget) = runCatching {
        budgetRepository.updateBudget(budget)
    }
}