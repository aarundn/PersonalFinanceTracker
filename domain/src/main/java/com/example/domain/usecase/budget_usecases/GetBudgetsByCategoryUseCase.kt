package com.example.domain.usecase.budget_usecases

import com.example.domain.model.Budget
import com.example.domain.repo.BudgetRepository
import kotlinx.coroutines.flow.Flow

class GetBudgetsByCategoryUseCase(
    private val budgetRepository: BudgetRepository
) {
    operator fun invoke(category: String): Flow<List<Budget>> =
        budgetRepository.getBudgetsByCategory(category)
}
