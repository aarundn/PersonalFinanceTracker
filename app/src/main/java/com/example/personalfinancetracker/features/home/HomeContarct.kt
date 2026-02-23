package com.example.personalfinancetracker.features.home

import androidx.compose.runtime.Immutable
import com.example.personalfinancetracker.features.budget.model.BudgetUi

/**
 * UDF contract for Home feature
 */
object HomeContract {

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

    sealed interface Event {
        data object OnClickAddExpense : Event
        data object OnClickAddIncome : Event
        data object OnClickCurrency : Event
        data class OnClickBudgetItem(val budgetId: String) : Event
        data object OnClickSavings : Event
        data object OnClickSettings : Event
        data object OnRetry : Event
    }

    sealed interface SideEffect {
        data object NavigateAddExpense : SideEffect
        data object NavigateAddIncome : SideEffect
        data object NavigateCurrency : SideEffect
        data object NavigateSettings : SideEffect
        data class ShowMessage(val message: String) : SideEffect
    }
}
