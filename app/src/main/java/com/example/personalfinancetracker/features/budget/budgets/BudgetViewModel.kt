package com.example.personalfinancetracker.features.budget.budgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BudgetViewModel(
    private val repository: BudgetRepository = InMemoryBudgetRepository()
) : ViewModel() {
    private val _state = MutableStateFlow(BudgetContract.State())
    val state: StateFlow<BudgetContract.State> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<BudgetContract.SideEffect>()
    val effects: SharedFlow<BudgetContract.SideEffect> = _effects.asSharedFlow()

    init {
        loadBudgets()
    }

    fun onEvent(event: BudgetContract.Event) {
        when (event) {
            BudgetContract.Event.LoadBudgets -> loadBudgets()
            is BudgetContract.Event.OnBudgetClick -> {
                viewModelScope.launch {
                    _effects.emit(BudgetContract.SideEffect.NavigateToBudgetDetails(event.budget.id))
                }
            }
            BudgetContract.Event.OnAddBudgetClick -> {
                viewModelScope.launch {
                    _effects.emit(BudgetContract.SideEffect.NavigateToAddBudget)
                }
            }
            BudgetContract.Event.OnRetry -> loadBudgets()
        }
    }

    private fun loadBudgets() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)

                // Get budgets from repository
                val budgets = repository.getBudgets().ifEmpty {
                    // Sample data if repository is empty
                    listOf(
                        BudgetContract.Budget(
                            id = 1,
                            category = "Food",
                            budgeted = 500.0,
                            spent = 385.0,
                            period = "monthly",
                            notes = "Includes groceries and dining out",
                            icon = Icons.Outlined.Check,
                            iconTint = Color(0xFFF97316), // Orange
                            iconBackground = Color(0xFFFEF3C7) // Orange-50
                        ),
                        BudgetContract.Budget(
                            id = 2,
                            category = "Transport",
                            budgeted = 300.0,
                            spent = 265.0,
                            period = "monthly",
                            notes = "Gas, parking, and public transport",
                            icon = Icons.Outlined.MailOutline,
                            iconTint = Color(0xFF3B82F6), // Blue
                            iconBackground = Color(0xFFEFF6FF) // Blue-50
                        ),
                        BudgetContract.Budget(
                            id = 3,
                            category = "Shopping",
                            budgeted = 400.0,
                            spent = 520.0,
                            period = "monthly",
                            notes = "Clothing and personal items",
                            icon = Icons.Outlined.ShoppingCart,
                            iconTint = Color(0xFF9333EA), // Purple
                            iconBackground = Color(0xFFF3E8FF) // Purple-50
                        ),
                        BudgetContract.Budget(
                            id = 4,
                            category = "Bills",
                            budgeted = 600.0,
                            spent = 420.0,
                            period = "monthly",
                            notes = "Utilities, internet, and subscriptions",
                            icon = Icons.Outlined.Home,
                            iconTint = Color(0xFF22C55E), // Green
                            iconBackground = Color(0xFFF0FDF4) // Green-50
                        ),
                        BudgetContract.Budget(
                            id = 5,
                            category = "Entertainment",
                            budgeted = 200.0,
                            spent = 180.0,
                            period = "monthly",
                            notes = "Movies, games, and events",
                            icon = Icons.Outlined.Star,
                            iconTint = Color(0xFFEC4899), // Pink
                            iconBackground = Color(0xFFFDF2F8) // Pink-50
                        )
                    )
                }

                val totalBudgeted = budgets.sumOf { it.budgeted }
                val totalSpent = budgets.sumOf { it.spent }
                val overallProgress = if (totalBudgeted > 0) (totalSpent / totalBudgeted).toFloat() else 0f

                _state.value = _state.value.copy(
                    budgets = budgets,
                    totalBudgeted = totalBudgeted,
                    totalSpent = totalSpent,
                    overallProgress = overallProgress,
                    isLoading = false,
                    error = null
                )
            } catch (_: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Failed to load budgets"
                )
                _effects.emit(BudgetContract.SideEffect.ShowError("Failed to load budgets"))
            }
        }
    }
}
