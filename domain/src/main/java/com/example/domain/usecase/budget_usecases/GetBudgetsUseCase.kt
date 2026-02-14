package com.example.domain.usecase.budget_usecases

import com.example.domain.repo.BudgetRepository

class GetBudgetsUseCase(
    private val budgetRepository: BudgetRepository
) {
    operator fun invoke() = budgetRepository.getAllBudgets()
}