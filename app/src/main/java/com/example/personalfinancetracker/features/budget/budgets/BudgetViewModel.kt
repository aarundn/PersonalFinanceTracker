package com.example.personalfinancetracker.features.budget.budgets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.budget_usecases.GetBudgetTransactionsUseCase
import com.example.domain.usecase.budget_usecases.GetBudgetsUseCase
import com.example.personalfinancetracker.features.budget.mapper.toBudgetUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class BudgetViewModel(
    getBudgetsUseCase: GetBudgetsUseCase,
    private val getBudgetTransactionsUseCase: GetBudgetTransactionsUseCase
) : ViewModel() {

    private val _sideEffect = MutableSharedFlow<BudgetsSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    val budgetsUiState: StateFlow<BudgetsUiState> =
        getBudgetsUseCase()
            .flatMapLatest<_, BudgetsUiState> { budgets ->
                if (budgets.isEmpty()) {
                    flowOf(BudgetsUiState.Success(emptyList()))
                } else {
                    val budgetFlows = budgets.map { budget ->
                        getBudgetTransactionsUseCase(budget).map { transactions ->
                            val spent = transactions.sumOf { it.amount }
                            budget.toBudgetUi(spent)
                        }
                    }
                    combine(budgetFlows) { budgetUis ->
                        BudgetsUiState.Success(budgets = budgetUis.toList())
                    }
                }
            }
            .onStart { emit(BudgetsUiState.Loading) }
            .catch { e ->
                emit(BudgetsUiState.Error(e.message ?: "Failed to load budgets"))
                _sideEffect.emit(
                    BudgetsSideEffect.ShowError(
                        e.message ?: "Failed to load budgets"
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = BudgetsUiState.Loading
            )

    fun onEvent(event: BudgetsEvent) {
        viewModelScope.launch {
            when (event) {
                is BudgetsEvent.OnBudgetClick -> {
                    _sideEffect.emit(
                        BudgetsSideEffect.NavigateToBudgetDetails(event.budget.id)
                    )
                }

                BudgetsEvent.OnAddBudgetClick -> {
                    _sideEffect.emit(BudgetsSideEffect.NavigateToAddBudget)
                }
            }
        }
    }
}
