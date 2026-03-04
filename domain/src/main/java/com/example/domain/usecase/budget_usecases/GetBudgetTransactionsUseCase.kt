package com.example.domain.usecase.budget_usecases

import com.example.domain.model.Transaction
import com.example.domain.repo.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetBudgetTransactionsUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(budgetId: String): Flow<List<Transaction>> {
        return transactionRepository.getTransactionsByBudgetId(budgetId)
    }
}

