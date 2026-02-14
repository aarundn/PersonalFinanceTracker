package com.example.domain.usecase.budget_usecases

import com.example.domain.repo.BudgetRepository

class DeleteBudgetUseCase(
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(id: String) = runCatching {
        budgetRepository.deleteBudgetById(id)
    }
}