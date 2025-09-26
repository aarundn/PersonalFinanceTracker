package com.example.personalfinancetracker.features.transaction.transactions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.ShoppingCart
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

class TransactionsViewModel(
    private val repository: TransactionsRepository = InMemoryTransactionsRepository()
) : ViewModel() {
    private val _state = MutableStateFlow(TransactionsContract.State())
    val state: StateFlow<TransactionsContract.State> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<TransactionsContract.SideEffect>()
    val effects: SharedFlow<TransactionsContract.SideEffect> = _effects.asSharedFlow()

    init {
        loadTransactions()
    }

    fun onEvent(event: TransactionsContract.Event) {
        when (event) {
            TransactionsContract.Event.LoadTransactions -> loadTransactions()
            is TransactionsContract.Event.OnTransactionClick -> {
                viewModelScope.launch {
                    _effects.emit(TransactionsContract.SideEffect.NavigateToTransactionDetails(event.transaction.id))
                }
            }

            TransactionsContract.Event.OnAddTransactionClick -> {
                viewModelScope.launch {
                    _effects.emit(TransactionsContract.SideEffect.NavigateToAddTransaction)
                }
            }

            TransactionsContract.Event.OnRetry -> loadTransactions()
        }
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)

                // Get transactions from repository
                val transactions = repository.getTransactions().ifEmpty {
                    // Sample data if repository is empty
                    listOf(
                        TransactionsContract.Transaction(
                            id = 1,
                            title = "Grocery Shopping",
                            category = "Food",
                            amount = -85.50,
                            date = "2024-03-15",
                            notes = "Weekly grocery shopping at Whole Foods",
                            location = "Whole Foods Market",
                            paymentMethod = "Credit Card",
                            icon = Icons.Outlined.ShoppingCart,
                            iconTint = Color(0xFFF97316) // Orange
                        ),
                        TransactionsContract.Transaction(
                            id = 2,
                            title = "Salary",
                            category = "Income",
                            amount = 5300.0,
                            date = "2024-03-15",
                            notes = "Monthly salary deposit",
                            paymentMethod = "Bank Transfer",
                            icon = Icons.Outlined.ShoppingCart,
                            iconTint = Color(0xFF22C55E) // Green
                        ),
                        TransactionsContract.Transaction(
                            id = 2,
                            title = "Salary",
                            category = "Income",
                            amount = 5300.0,
                            date = "2024-03-15",
                            notes = "Monthly salary deposit",
                            paymentMethod = "Bank Transfer",
                            icon = Icons.Outlined.ShoppingCart,
                            iconTint = Color(0xFF22C55E) // Green
                        ),
                        TransactionsContract.Transaction(
                            id = 2,
                            title = "Salary",
                            category = "Income",
                            amount = 5300.0,
                            date = "2024-03-15",
                            notes = "Monthly salary deposit",
                            paymentMethod = "Bank Transfer",
                            icon = Icons.Outlined.ShoppingCart,
                            iconTint = Color(0xFF22C55E) // Green
                        ),
                        TransactionsContract.Transaction(
                            id = 2,
                            title = "Salary",
                            category = "Income",
                            amount = 5300.0,
                            date = "2024-03-15",
                            notes = "Monthly salary deposit",
                            paymentMethod = "Bank Transfer",
                            icon = Icons.Outlined.ShoppingCart,
                            iconTint = Color(0xFF22C55E) // Green
                        ),
                        TransactionsContract.Transaction(
                            id = 2,
                            title = "Salary",
                            category = "Income",
                            amount = 5300.0,
                            date = "2024-03-15",
                            notes = "Monthly salary deposit",
                            paymentMethod = "Bank Transfer",
                            icon = Icons.Outlined.ShoppingCart,
                            iconTint = Color(0xFF22C55E) // Green
                        ),
                        TransactionsContract.Transaction(
                            id = 2,
                            title = "Salary",
                            category = "Income",
                            amount = 5300.0,
                            date = "2024-03-15",
                            notes = "Monthly salary deposit",
                            paymentMethod = "Bank Transfer",
                            icon = Icons.Outlined.ShoppingCart,
                            iconTint = Color(0xFF22C55E) // Green
                        ),
                        TransactionsContract.Transaction(
                            id = 3,
                            title = "Gas Station",
                            category = "Transport",
                            amount = -65.00,
                            date = "2024-03-14",
                            location = "Shell Gas Station",
                            paymentMethod = "Debit Card",
                            icon = Icons.Outlined.Place,
                            iconTint = Color(0xFF3B82F6) // Blue
                        ),
                        TransactionsContract.Transaction(
                            id = 4,
                            title = "Electricity Bill",
                            category = "Bills",
                            amount = -120.00,
                            date = "2024-03-13",
                            notes = "Monthly electricity bill",
                            paymentMethod = "Bank Transfer",
                            icon = Icons.Outlined.Home,
                            iconTint = Color(0xFF9333EA) // Purple
                        ),
                        TransactionsContract.Transaction(
                            id = 5,
                            title = "Coffee Shop",
                            category = "Food",
                            amount = -12.50,
                            date = "2024-03-13",
                            location = "Starbucks",
                            paymentMethod = "Digital Wallet",
                            icon = Icons.Outlined.MailOutline,
                            iconTint = Color(0xFFF59E0B) // Amber
                        ),
                        TransactionsContract.Transaction(
                            id = 6,
                            title = "Freelance Work",
                            category = "Income",
                            amount = 800.0,
                            date = "2024-03-12",
                            notes = "Website development project",
                            paymentMethod = "Bank Transfer",
                            icon = Icons.Outlined.Check,
                            iconTint = Color(0xFF22C55E) // Green
                        )
                    )
                }
                _state.value = _state.value.copy(
                    transactions = transactions,
                    isLoading = false,
                    error = null
                )
            } catch (_: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Failed to load transactions"
                )
                _effects.emit(TransactionsContract.SideEffect.ShowError("Failed to load transactions"))
            }
        }
    }
}