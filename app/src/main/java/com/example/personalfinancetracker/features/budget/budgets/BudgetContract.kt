package com.example.personalfinancetracker.features.budget.budgets

import androidx.compose.runtime.Immutable
import com.example.personalfinancetracker.features.budget.model.BudgetUi

@Immutable
sealed interface BudgetsUiState {
    data object Loading : BudgetsUiState
    data class Success(val budgets: List<BudgetUi>) : BudgetsUiState
    data class Error(val message: String) : BudgetsUiState
}

sealed interface BudgetsEvent {
    data class OnBudgetClick(val budget: BudgetUi) : BudgetsEvent
    data object OnAddBudgetClick : BudgetsEvent
}

sealed interface BudgetsSideEffect {
    data object NavigateToAddBudget : BudgetsSideEffect
    data class NavigateToBudgetDetails(val budgetId: String) : BudgetsSideEffect
    data class ShowError(val message: String) : BudgetsSideEffect
}
