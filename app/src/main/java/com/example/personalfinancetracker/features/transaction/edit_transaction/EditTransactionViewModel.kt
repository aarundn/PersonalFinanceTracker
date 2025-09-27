package com.example.personalfinancetracker.features.transaction.edit_transaction

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class EditTransactionViewModel (
    // Add repository injection here when available
) : ViewModel() {

    private val _state = MutableStateFlow(EditTransactionContract.State())
    val state: StateFlow<EditTransactionContract.State> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<EditTransactionContract.SideEffect>()
    val sideEffect: SharedFlow<EditTransactionContract.SideEffect> = _sideEffect.asSharedFlow()

    fun onEvent(event: EditTransactionContract.Event) {
        when (event) {
            is EditTransactionContract.Event.OnTransactionTypeChanged -> {
                _state.value = _state.value.copy(isIncome = event.isIncome)
            }
            is EditTransactionContract.Event.OnTitleChanged -> {
                _state.value = _state.value.copy(title = event.title)
            }
            is EditTransactionContract.Event.OnCategoryChanged -> {
                _state.value = _state.value.copy(category = event.category)
            }
            is EditTransactionContract.Event.OnAmountChanged -> {
                _state.value = _state.value.copy(amount = event.amount)
            }
            is EditTransactionContract.Event.OnCurrencyChanged -> {
                _state.value = _state.value.copy(currency = event.currency)
            }
            is EditTransactionContract.Event.OnDateChanged -> {
                _state.value = _state.value.copy(date = event.date)
            }
            is EditTransactionContract.Event.OnNotesChanged -> {
                _state.value = _state.value.copy(notes = event.notes)
            }
            is EditTransactionContract.Event.OnLocationChanged -> {
                _state.value = _state.value.copy(location = event.location)
            }
            is EditTransactionContract.Event.OnPaymentMethodChanged -> {
                _state.value = _state.value.copy(paymentMethod = event.paymentMethod)
            }
            EditTransactionContract.Event.OnConvertCurrency -> {
                // Handle currency conversion logic here
                _state.value = _state.value.copy(isConverting = true)
                // Simulate conversion
                viewModelScope.launch {
                    kotlinx.coroutines.delay(1000)
                    _state.value = _state.value.copy(
                        isConverting = false,
                        convertedAmount = _state.value.amount.toDoubleOrNull()?.times(1.2) // Mock conversion
                    )
                }
            }
            EditTransactionContract.Event.OnSave -> {
                saveTransaction()
            }
            EditTransactionContract.Event.OnCancel -> {
                viewModelScope.launch {
                    _sideEffect.emit(EditTransactionContract.SideEffect.NavigateBack)
                }
            }
            EditTransactionContract.Event.OnDelete -> {
                viewModelScope.launch {
                    _sideEffect.emit(
                        EditTransactionContract.SideEffect.ShowDeleteConfirmation(
                            _state.value.transactionId
                        )
                    )
                }
            }
            EditTransactionContract.Event.OnEdit -> {
                _state.value = _state.value.copy(isEditing = true)
            }
            EditTransactionContract.Event.OnLoadTransaction -> {
                loadTransaction()
            }
        }
    }

    private fun saveTransaction() {
        val currentState = _state.value
        
        // Validate required fields
        if (currentState.title.isBlank() || currentState.category.isBlank() || currentState.amount.isBlank()) {
            viewModelScope.launch {
                _sideEffect.emit(
                    EditTransactionContract.SideEffect.ShowError("Please fill in all required fields")
                )
            }
            return
        }

        _state.value = currentState.copy(isLoading = true)

        viewModelScope.launch {
            try {
                // Simulate API call
                kotlinx.coroutines.delay(1000)
                
                // Here you would call your repository to update the transaction
                // repository.updateTransaction(createTransactionData())
                
                _state.value = currentState.copy(
                    isLoading = false,
                    isEditing = false
                )
                
                _sideEffect.emit(
                    EditTransactionContract.SideEffect.ShowSuccess("Transaction updated successfully")
                )
                
                _sideEffect.emit(EditTransactionContract.SideEffect.NavigateBack)
                
            } catch (e: Exception) {
                _state.value = currentState.copy(
                    isLoading = false,
                    error = e.message
                )
                _sideEffect.emit(
                    EditTransactionContract.SideEffect.ShowError(e.message ?: "Failed to update transaction")
                )
            }
        }
    }

    private fun loadTransaction() {
        _state.value = _state.value.copy(isLoading = true)
        
        viewModelScope.launch {
            try {
                // Simulate loading transaction data
                kotlinx.coroutines.delay(500)
                
                // Here you would load the transaction from repository
                // val transaction = repository.getTransactionById(transactionId)
                // populateStateWithTransaction(transaction)
                
                // Mock data based on transaction ID - simulating different transactions
                val mockTransaction = getMockTransactionById(_state.value.transactionId)
                
                _state.value = _state.value.copy(
                    isLoading = false,
                    isIncome = mockTransaction.amount > 0,
                    title = mockTransaction.title,
                    category = mockTransaction.category,
                    amount = kotlin.math.abs(mockTransaction.amount).toString(),
                    currency = "USD",
                    date = mockTransaction.date,
                    notes = mockTransaction.notes ?: "",
                    location = mockTransaction.location ?: "",
                    paymentMethod = mockTransaction.paymentMethod,
                    icon = mockTransaction.icon,
                    iconTint = mockTransaction.iconTint
                )
                
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
                _sideEffect.emit(
                    EditTransactionContract.SideEffect.ShowError(e.message ?: "Failed to load transaction")
                )
            }
        }
    }
    
    private fun getMockTransactionById(transactionId: Int): MockTransaction {
        return when (transactionId) {
            1 -> MockTransaction(
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
            )
            2 -> MockTransaction(
                id = 2,
                title = "Freelance Work",
                category = "Income",
                amount = 800.0,
                date = "2024-03-12",
                notes = "Website development project",
                paymentMethod = "Bank Transfer",
                icon = Icons.Outlined.ShoppingCart,
                iconTint = Color(0xFF22C55E) // Green
            )
            3 -> MockTransaction(
                id = 3,
                title = "Gas Station",
                category = "Transport",
                amount = -65.00,
                date = "2024-03-14",
                location = "Shell Gas Station",
                paymentMethod = "Debit Card",
                icon = Icons.Outlined.Home,
                iconTint = Color(0xFF3B82F6) // Blue
            )
            4 -> MockTransaction(
                id = 4,
                title = "Electricity Bill",
                category = "Bills",
                amount = -120.00,
                date = "2024-03-13",
                notes = "Monthly electricity bill",
                paymentMethod = "Bank Transfer",
                icon = Icons.Outlined.Home,
                iconTint = Color(0xFF9333EA) // Purple
            )
            5 -> MockTransaction(
                id = 5,
                title = "Coffee Shop",
                category = "Food",
                amount = -12.50,
                date = "2024-03-13",
                location = "Starbucks",
                paymentMethod = "Digital Wallet",
                icon = Icons.Outlined.Home,
                iconTint = Color(0xFFF59E0B) // Amber
            )
            6 -> MockTransaction(
                id = 6,
                title = "Salary",
                category = "Income",
                amount = 5300.0,
                date = "2024-03-15",
                notes = "Monthly salary deposit",
                paymentMethod = "Bank Transfer",
                icon = Icons.Outlined.Check,
                iconTint = Color(0xFF22C55E) // Green
            )
            else -> MockTransaction(
                id = transactionId,
                title = "Sample Transaction",
                category = "Other",
                amount = -25.50,
                date = "2024-01-15",
                notes = "Sample transaction",
                location = "Sample Location",
                paymentMethod = "Credit Card",
                icon = Icons.Outlined.Place,
                iconTint = Color(0xFF6B7280) // Gray
            )
        }
    }
    
    private data class MockTransaction(
        val id: Int,
        val title: String,
        val category: String,
        val amount: Double,
        val date: String,
        val notes: String? = null,
        val location: String? = null,
        val paymentMethod: String,
        val icon: ImageVector,
        val iconTint: Color
    )

    private fun createTransactionData(): EditTransactionContract.TransactionData {
        val currentState = _state.value
        return EditTransactionContract.TransactionData(
            id = currentState.transactionId,
            isIncome = currentState.isIncome,
            title = currentState.title,
            category = currentState.category,
            amount = currentState.amount.toDoubleOrNull() ?: 0.0,
            originalAmount = currentState.amount.toDoubleOrNull() ?: 0.0,
            currency = currentState.currency,
            baseCurrency = "USD",
            date = currentState.date,
            notes = currentState.notes,
            location = currentState.location,
            paymentMethod = currentState.paymentMethod
        )
    }

    fun setTransactionId(transactionId: Int) {
        _state.value = _state.value.copy(transactionId = transactionId)
    }
}