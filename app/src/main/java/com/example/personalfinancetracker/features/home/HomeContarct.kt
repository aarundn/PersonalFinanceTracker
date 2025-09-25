package com.example.personalfinancetracker.features.home

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * UDF contract for Home feature
 */
object HomeContract {
    @Immutable
    data class State(
        val greeting: String = "Good morning!",
        val subtitle: String = "Here's your financial overview for today",
        val income: MoneyCard = MoneyCard(label = "Income", amount = 5300.0),
        val expenses: MoneyCard = MoneyCard(label = "Expense", amount = 4100.0),
        val balance: MoneyCard = MoneyCard(label = "Balance", amount = 1200.0),
        val monthStats: MonthStats = MonthStats(),
        val budgets: List<BudgetProgress> = emptyList(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )

    @Immutable
    data class MoneyCard(
        val label: String,
        val amount: Double,
        val color: Color? = null
    )

    @Immutable
    data class MonthStats(
        val totalTransactions: Int = 42,
        val dailyAverage: Double = 136.67,
        val daysPassed: Int = 15,
        val daysInMonth: Int = 30
    )

    @Immutable
    data class BudgetProgress(
        val name: String,
        val spent: Double,
        val limit: Double,
        val color: Color? = null
    ) {
        val progress: Float get() = if (limit == 0.0) 0f else (spent / limit).toFloat().coerceIn(0f, 1f)
    }


    sealed interface Event {
        data object OnClickBudgets : Event
        data object OnClickTransactions : Event
        data object OnClickAddExpense : Event
        data object OnClickAddIncome : Event
        data object OnClickCurrency : Event
        data class OnClickBudgetItem(val name: String) : Event
        data object OnRetry : Event
    }

    sealed interface SideEffect {
        data object NavigateBudgets : SideEffect
        data object NavigateTransactions : SideEffect
        data object NavigateAddExpense : SideEffect
        data object NavigateAddIncome : SideEffect
        data object NavigateCurrency : SideEffect
        data class ShowMessage(val message: String) : SideEffect
    }
}

interface HomeRepository {
    fun initialHomeState(): HomeContract.State
}

class InMemoryHomeRepository : HomeRepository {
    override fun initialHomeState(): HomeContract.State {
        return HomeContract.State(
            budgets = listOf(
                HomeContract.BudgetProgress(name = "Food", spent = 10.0, limit = 500.0),
                HomeContract.BudgetProgress(name = "Transport", spent = 2.0, limit = 300.0),
                HomeContract.BudgetProgress(name = "Shopping", spent = 520.0, limit = 400.0),
                HomeContract.BudgetProgress(name = "Bills", spent = 420.0, limit = 600.0)
            )
        )
    }
}

