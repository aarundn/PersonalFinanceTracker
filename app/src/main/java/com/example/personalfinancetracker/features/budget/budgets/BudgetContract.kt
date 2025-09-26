package com.example.personalfinancetracker.features.budget.budgets

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * UDF contract for Budget feature
 */
object BudgetContract {
    @Immutable
    data class State(
        val budgets: List<Budget> = emptyList(),
        val totalBudgeted: Double = 0.0,
        val totalSpent: Double = 0.0,
        val overallProgress: Float = 0f,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    @Immutable
    data class Budget(
        val id: Int,
        val category: String,
        val budgeted: Double,
        val spent: Double,
        val period: String,
        val notes: String? = null,
        val icon: ImageVector,
        val iconTint: Color,
        val iconBackground: Color
    ) {
        val percentage: Float get() = if (budgeted == 0.0) 0f else (spent / budgeted).toFloat().coerceIn(0f, 1f)
        val remaining: Double get() = (budgeted - spent).coerceAtLeast(0.0)
        val overBudget: Double get() = (spent - budgeted).coerceAtLeast(0.0)
        val isOverBudget: Boolean get() = spent > budgeted
        val isWarning: Boolean get() = percentage > 0.8f && !isOverBudget
    }

    sealed interface Event {
        data object LoadBudgets : Event
        data class OnBudgetClick(val budget: Budget) : Event
        data object OnAddBudgetClick : Event
        data object OnRetry : Event
    }

    sealed interface SideEffect {
        data object NavigateToAddBudget : SideEffect
        data class NavigateToBudgetDetails(val budgetId: Int) : SideEffect
        data class ShowError(val message: String) : SideEffect
    }
}

interface BudgetRepository {
    fun getBudgets(): List<BudgetContract.Budget>
}

class InMemoryBudgetRepository : BudgetRepository {
    override fun getBudgets(): List<BudgetContract.Budget> {
        return emptyList() // Will be populated by ViewModel
    }
}
