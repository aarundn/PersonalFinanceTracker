package com.example.personalfinancetracker.features.home

import androidx.compose.runtime.Immutable
import com.example.core.common.UiText
import com.example.personalfinancetracker.features.transaction.model.TransactionUi

enum class Timeframe {
    WEEKLY, MONTHLY
}

@Immutable
data class BarChartItem(
    val label: String,
    val value: Float, // 0.0 to 1.0 for height relative to max
    val amountString: String,
    val isSelected: Boolean = false
)

@Immutable
data class BudgetUiModel(
    val id: String,
    val categoryName: String,
    val percentage: Float, // 0.0 to 1.0
    val colorHex: Long // e.g., 0xFFA855F7
)

@Immutable
data class HomeTransactionUiModel(
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: String,
    val time: String,
    val isIncome: Boolean,
    val colorHex: Long
)

@Immutable
data class HomeData(
    val userName: String,
    val totalBalance: String,
    val balancePercentageChange: String,
    val selectedAnalysisTimeframe: Timeframe,
    val barChartData: List<BarChartItem>,
    val dailyAverage: String,
    val budgets: List<BudgetUiModel>,
    val recentTransactions: List<TransactionUi>
)

@Immutable
sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val data: HomeData) : HomeUiState
    data class Error(val message: UiText) : HomeUiState
}

sealed interface HomeEvent {
    data object OnLoadHomeData : HomeEvent
    data object OnClickAddTransaction : HomeEvent
    data object OnClickTransfer : HomeEvent
    data class OnAnalysisTimeframeChanged(val timeframe: Timeframe) : HomeEvent
    data class OnClickTransaction(val transactionId: String) : HomeEvent
    data object OnClickViewAllTransactions : HomeEvent
    data object OnClickViewAllBudgets : HomeEvent
    data class OnClickBudget(val budgetId: String) : HomeEvent
    data object OnClickSettings : HomeEvent
}

sealed interface HomeSideEffect {
    data object NavigateAddTransaction : HomeSideEffect
    data object NavigateTransfer : HomeSideEffect
    data class NavigateTransactionDetails(val transactionId: String) : HomeSideEffect
    data object NavigateAllTransactions : HomeSideEffect
    data object NavigateAllBudgets : HomeSideEffect
    data object NavigateSettings : HomeSideEffect
    data class ShowMessage(val message: UiText) : HomeSideEffect
}
