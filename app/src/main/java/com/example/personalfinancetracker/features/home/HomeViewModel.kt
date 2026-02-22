package com.example.personalfinancetracker.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Type
import com.example.domain.usecase.budget_usecases.GetBudgetsUseCase
import com.example.domain.usecase.transaction_usecases.GetTransactionsUseCase
import com.example.personalfinancetracker.features.budget.mapper.toBudgetUi
import com.example.personalfinancetracker.features.home.HomeContract.Event
import com.example.personalfinancetracker.features.home.HomeContract.HomeData
import com.example.personalfinancetracker.features.home.HomeContract.HomeUiState
import com.example.personalfinancetracker.features.home.HomeContract.SideEffect
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime


class HomeViewModel(
    getTransactionsUseCase: GetTransactionsUseCase,
    getBudgetsUseCase: GetBudgetsUseCase,
) : ViewModel() {

    private val _sideEffect = MutableSharedFlow<SideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    @OptIn(ExperimentalTime::class)
    val homeUiState: StateFlow<HomeUiState> =
        combine(
            getTransactionsUseCase(),
            getBudgetsUseCase()
        ) { transactions, budgets ->
            val income = transactions
                .filter { it.type == Type.INCOME }
                .sumOf { it.amount }

            val expense = transactions
                .filter { it.type == Type.EXPENSE }
                .sumOf { it.amount }

            val budgetUis = budgets.map { budget ->
                val spent = transactions
                    .filter { it.type == Type.EXPENSE && it.category == budget.category }
                    .sumOf { it.amount }
                budget.toBudgetUi(spent)
            }

            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            val daysPassed = now.dayOfMonth
            val daysInMonth = now.month.length(isLeapYear(now.year))

            HomeUiState.Success(
                HomeData(
                    greeting = computeGreeting(now.hour),
                    subtitle = "Here's your financial overview",
                    totalIncome = income,
                    totalExpense = expense,
                    balance = income - expense,
                    totalTransactions = transactions.size,
                    dailyAverage = if (daysPassed > 0) expense / daysPassed else 0.0,
                    daysPassed = daysPassed,
                    daysInMonth = daysInMonth,
                    budgets = budgetUis,
                )
            ) as HomeUiState
        }
            .onStart { emit(HomeUiState.Loading ) }
            .catch { e -> emit(HomeUiState.Error(e.message ?: "Failed to load data")) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HomeUiState.Loading,
            )

    fun onEvent(event: Event) {
        viewModelScope.launch {
            when (event) {
                Event.OnClickAddExpense -> _sideEffect.emit(SideEffect.NavigateAddExpense)
                Event.OnClickAddIncome -> _sideEffect.emit(SideEffect.NavigateAddIncome)
                Event.OnClickBudgets -> _sideEffect.emit(SideEffect.NavigateBudgets)
                Event.OnClickTransactions -> _sideEffect.emit(SideEffect.NavigateTransactions)
                Event.OnClickCurrency -> _sideEffect.emit(SideEffect.NavigateCurrency)
                is Event.OnClickBudgetItem -> _sideEffect.emit(SideEffect.ShowMessage("${event.budgetId} tapped"))
                Event.OnClickSavings -> _sideEffect.emit(SideEffect.ShowMessage("Savings tapped"))
                Event.OnRetry -> { /* State is reactive — no manual retry needed */ }
            }
        }
    }

    private fun computeGreeting(hour: Int): String = when (hour) {
        in 5..11 -> "Good morning!"
        in 12..17 -> "Good afternoon!"
        in 18..21 -> "Good evening!"
        else -> "Good night!"
    }

    private fun isLeapYear(year: Int): Boolean =
        (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}
