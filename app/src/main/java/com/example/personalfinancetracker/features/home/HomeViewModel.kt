package com.example.personalfinancetracker.features.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conversion_rate.domain.usecase.ConvertCurrencyUseCase
import com.example.core.model.DefaultCurrencies
import com.example.domain.model.Budget
import com.example.domain.model.Transaction
import com.example.domain.model.Type
import com.example.domain.repo.UserPreferencesRepository
import com.example.domain.usecase.budget_usecases.GetBudgetsUseCase
import com.example.domain.usecase.transaction_usecases.GetTransactionsUseCase
import com.example.personalfinancetracker.features.budget.mapper.toBudgetUi
import com.example.personalfinancetracker.features.budget.model.BudgetUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.math.BigDecimal
import kotlin.time.ExperimentalTime

class HomeViewModel(
    getTransactionsUseCase: GetTransactionsUseCase,
    getBudgetsUseCase: GetBudgetsUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val convertCurrencyUseCase: ConvertCurrencyUseCase,
    private val clock: Clock,
) : ViewModel() {

    private val _sideEffect = MutableSharedFlow<HomeSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalTime::class)
    val homeUiState: StateFlow<HomeUiState> =
        combine(
            getTransactionsUseCase(),
            getBudgetsUseCase(),
            userPreferencesRepository.baseCurrency,
            userPreferencesRepository.selectedProviderId,
        ) { transactions, budgets, baseCurrencyId, providerId ->
            try {
                val rates = getCurrencyRates(
                    transactions = transactions,
                    budgets = budgets,
                    baseCurrencyId = baseCurrencyId,
                    providerId = providerId
                )

                val convertedTransactions = convertTransactions(transactions, baseCurrencyId, rates)
                val income = calculateTotal(convertedTransactions, Type.INCOME)
                val expense = calculateTotal(convertedTransactions, Type.EXPENSE)
                val budgetUis =
                    mapBudgetsToUi(budgets, convertedTransactions, baseCurrencyId, rates)

                val now = clock.now().toLocalDateTime(TimeZone.currentSystemDefault())
                val daysPassed = now.dayOfMonth
                val daysInMonth = getDaysInMonth(now)

                val currencySymbol = DefaultCurrencies.fromId(baseCurrencyId)?.symbol ?: "$"

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
                        currencySymbol = currencySymbol
                    )
                ) as HomeUiState
            } catch (e: Exception) {
                HomeUiState.Error(e.message ?: "Failed to load data")
            }
        }
            .flowOn(Dispatchers.IO)
            .onStart { emit(HomeUiState.Loading) }
            .catch { e -> emit(HomeUiState.Error(e.message ?: "Failed to load data")) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HomeUiState.Loading,
            )

    fun onEvent(event: HomeEvent) {
        viewModelScope.launch {
            when (event) {
                HomeEvent.OnClickAddExpense -> _sideEffect.emit(HomeSideEffect.NavigateAddExpense)
                HomeEvent.OnClickAddIncome -> _sideEffect.emit(HomeSideEffect.NavigateAddIncome)
                HomeEvent.OnClickCurrency -> _sideEffect.emit(HomeSideEffect.NavigateCurrency)
                HomeEvent.OnClickSettings -> _sideEffect.emit(HomeSideEffect.NavigateSettings)
                is HomeEvent.OnClickBudgetItem -> _sideEffect.emit(HomeSideEffect.ShowMessage("${event.budgetId} tapped"))
                HomeEvent.OnClickSavings -> _sideEffect.emit(HomeSideEffect.ShowMessage("Savings tapped"))
                HomeEvent.OnRetry -> { /* State is reactive — no manual retry needed */
                }
            }
        }
    }

    private fun computeGreeting(hour: Int): String = when (hour) {
        in 5..11 -> "Good morning!"
        in 12..17 -> "Good afternoon!"
        in 18..21 -> "Good evening!"
        else -> "Good night!"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDaysInMonth(now: kotlinx.datetime.LocalDateTime): Int {
        return now.month.length(isLeapYear(now.year))
    }

    private fun isLeapYear(year: Int): Boolean =
        (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)


    private suspend fun getCurrencyRates(
        transactions: List<Transaction>,
        budgets: List<Budget>,
        baseCurrencyId: String,
        providerId: String
    ): Map<String, Double> {
        val uniqueCurrencies =
            (transactions.map { it.currency } + budgets.map { it.currency }).toSet() - baseCurrencyId

        return uniqueCurrencies.associateWith { currency ->
            val convertedAmountResult = convertCurrencyUseCase(
                providerId = providerId,
                fromCurrencyCode = currency,
                toCurrencyCode = baseCurrencyId,
                amount = BigDecimal.ONE
            )
            convertedAmountResult.getOrThrow().toDouble()
        }
    }

    private fun convertTransactions(
        transactions: List<Transaction>,
        baseCurrencyId: String,
        rates: Map<String, Double>
    ): List<Transaction> {
        return transactions.map { transaction ->
            if (transaction.currency == baseCurrencyId) {
                transaction
            } else {
                val rate = rates[transaction.currency] ?: 1.0
                val convertedAmount = transaction.amount * rate
                transaction.copy(amount = convertedAmount, currency = baseCurrencyId)
            }
        }
    }

    private fun calculateTotal(
        transactions: List<Transaction>,
        type: Type
    ): Double {
        return transactions
            .filter { it.type == type }
            .sumOf { it.amount }
    }

    private fun mapBudgetsToUi(
        budgets: List<Budget>,
        convertedTransactions: List<Transaction>,
        baseCurrencyId: String,
        rates: Map<String, Double>
    ): List<BudgetUi> {
        return budgets.map { budget ->
            val spent = convertedTransactions
                .filter { it.type == Type.EXPENSE && it.category == budget.category }
                .sumOf { it.amount }

            val budgetLimitConverted = if (budget.currency == baseCurrencyId) {
                budget.amount
            } else {
                val rate = rates[budget.currency] ?: 1.0
                budget.amount * rate
            }

            budget.copy(amount = budgetLimitConverted, currency = baseCurrencyId).toBudgetUi(spent)
        }
    }
}
