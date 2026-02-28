package com.example.personalfinancetracker.features.home

import androidx.compose.runtime.Immutable
import com.example.personalfinancetracker.features.budget.model.BudgetUi

@Immutable
data class HomeData(
    val greeting: String,
    val subtitle: String,
    val totalIncome: Double,
    val totalExpense: Double,
    val balance: Double,
    val totalTransactions: Int,
    val dailyAverage: Double,
    val daysPassed: Int,
    val daysInMonth: Int,
    val budgets: List<BudgetUi>,
    val currencySymbol: String
)

@Immutable
sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val data: HomeData) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

sealed interface HomeEvent {
    data object OnClickAddExpense : HomeEvent
    data object OnClickAddIncome : HomeEvent
    data object OnClickCurrency : HomeEvent
    data class OnClickBudgetItem(val budgetId: String) : HomeEvent
    data object OnClickSavings : HomeEvent
    data object OnClickSettings : HomeEvent
    data object OnRetry : HomeEvent
}

sealed interface HomeSideEffect {
    data object NavigateAddExpense : HomeSideEffect
    data object NavigateAddIncome : HomeSideEffect
    data object NavigateCurrency : HomeSideEffect
    data object NavigateSettings : HomeSideEffect
    data class ShowMessage(val message: String) : HomeSideEffect
}
