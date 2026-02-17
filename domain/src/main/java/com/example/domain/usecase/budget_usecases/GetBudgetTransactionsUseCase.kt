package com.example.domain.usecase.budget_usecases

import com.example.domain.model.Budget
import com.example.domain.model.Transaction
import com.example.domain.model.Type
import com.example.domain.repo.BudgetRepository
import com.example.domain.repo.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus

class GetBudgetTransactionsUseCase(
    private val transactionRepository: TransactionRepository,
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(budgetId: String): Flow<List<Transaction>> {
        val budget = budgetRepository.getBudgetById(budgetId)
            ?: throw IllegalArgumentException("Budget not found: $budgetId")

        return invoke(budget)
    }

    operator fun invoke(budget: Budget): Flow<List<Transaction>> {
        val periodEnd = calculatePeriodEnd(budget.createdAt, budget.period)

        return transactionRepository.getTransactionsByCategoryAndDateRange(
            category = budget.category,
            currency = budget.currency,
            type = Type.EXPENSE,
            startDate = budget.createdAt,
            endDate = periodEnd
        )
    }

    private fun calculatePeriodEnd(createdAt: Long, period: String): Long {
        val timeZone = TimeZone.currentSystemDefault()
        val startDate = Instant.fromEpochMilliseconds(createdAt)
        val endDate = when (period.lowercase()) {
            "weekly" -> startDate.plus(1, DateTimeUnit.WEEK, timeZone)
            "monthly" -> startDate.plus(1, DateTimeUnit.MONTH, timeZone)
            "quarterly" -> startDate.plus(3, DateTimeUnit.MONTH, timeZone)
            "yearly" -> startDate.plus(1, DateTimeUnit.YEAR, timeZone)
            else -> startDate.plus(1, DateTimeUnit.MONTH, timeZone)
        }
        return endDate.toEpochMilliseconds()
    }
}

