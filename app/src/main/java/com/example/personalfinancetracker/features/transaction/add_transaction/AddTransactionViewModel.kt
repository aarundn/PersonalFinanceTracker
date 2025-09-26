package com.example.personalfinancetracker.features.transaction.add_transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddTransactionViewModel : ViewModel() {

    private val _state = MutableStateFlow(
        AddTransactionContract.State(
            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        )
    )
    val state: StateFlow<AddTransactionContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AddTransactionContract.SideEffect>()
    val effect: SharedFlow<AddTransactionContract.SideEffect> = _effect.asSharedFlow()

    fun onEvent(event: AddTransactionContract.Event) {
        when (event) {
            is AddTransactionContract.Event.OnTransactionTypeChanged -> {
                _state.value = _state.value.copy(
                    isIncome = event.isIncome,
                    category = "" // Reset category when type changes
                )
            }
            is AddTransactionContract.Event.OnTitleChanged -> {
                _state.value = _state.value.copy(title = event.title)
            }
            is AddTransactionContract.Event.OnCategoryChanged -> {
                _state.value = _state.value.copy(category = event.category)
            }
            is AddTransactionContract.Event.OnAmountChanged -> {
                _state.value = _state.value.copy(amount = event.amount)
                // Trigger currency conversion if needed
                if (event.amount.isNotEmpty() && _state.value.currency != "USD") {
                    convertCurrency()
                }
            }
            is AddTransactionContract.Event.OnCurrencyChanged -> {
                _state.value = _state.value.copy(currency = event.currency)
                // Trigger currency conversion if amount exists
                if (_state.value.amount.isNotEmpty() && event.currency != "USD") {
                    convertCurrency()
                }
            }
            is AddTransactionContract.Event.OnDateChanged -> {
                _state.value = _state.value.copy(date = event.date)
            }
            is AddTransactionContract.Event.OnNotesChanged -> {
                _state.value = _state.value.copy(notes = event.notes)
            }
            AddTransactionContract.Event.OnConvertCurrency -> {
                convertCurrency()
            }
            AddTransactionContract.Event.OnSave -> {
                saveTransaction()
            }
            AddTransactionContract.Event.OnCancel -> {
                viewModelScope.launch {
                    _effect.emit(AddTransactionContract.SideEffect.NavigateBack)
                }
            }
        }
    }

    private fun convertCurrency() {
        val currentState = _state.value
        if (currentState.amount.isEmpty() || currentState.currency == "USD") {
            _state.value = currentState.copy(convertedAmount = null)
            return
        }

        _state.value = currentState.copy(isConverting = true)
        
        viewModelScope.launch {
            try {
                // Simulate currency conversion API call
                kotlinx.coroutines.delay(1000)
                
                val amount = currentState.amount.toDoubleOrNull() ?: 0.0
                // Mock conversion rate (in real app, this would be from API)
                val conversionRate = when (currentState.currency) {
                    "EUR" -> 0.85
                    "GBP" -> 0.73
                    "JPY" -> 110.0
                    "CAD" -> 1.25
                    "AUD" -> 1.35
                    "CHF" -> 0.92
                    "CNY" -> 6.45
                    "INR" -> 74.0
                    "BRL" -> 5.2
                    else -> 1.0
                }
                
                val convertedAmount = amount / conversionRate
                _state.value = currentState.copy(
                    convertedAmount = convertedAmount,
                    isConverting = false
                )
            } catch (e: Exception) {
                _state.value = currentState.copy(
                    isConverting = false,
                    error = "Currency conversion failed"
                )
                _effect.emit(AddTransactionContract.SideEffect.ShowError("Currency conversion failed"))
            }
        }
    }

    private fun saveTransaction() {
        val currentState = _state.value
        
        // Validate required fields
        if (currentState.title.isEmpty()) {
            viewModelScope.launch {
                _effect.emit(AddTransactionContract.SideEffect.ShowError("Title is required"))
            }
            return
        }
        
        if (currentState.category.isEmpty()) {
            viewModelScope.launch {
                _effect.emit(AddTransactionContract.SideEffect.ShowError("Category is required"))
            }
            return
        }
        
        if (currentState.amount.isEmpty()) {
            viewModelScope.launch {
                _effect.emit(AddTransactionContract.SideEffect.ShowError("Amount is required"))
            }
            return
        }

        val amount = currentState.amount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            viewModelScope.launch {
                _effect.emit(AddTransactionContract.SideEffect.ShowError("Please enter a valid amount"))
            }
            return
        }

        _state.value = currentState.copy(isLoading = true)

        viewModelScope.launch {
            try {
                // Simulate save operation
                kotlinx.coroutines.delay(1000)
                
                val transactionData = AddTransactionContract.TransactionData(
                    isIncome = currentState.isIncome,
                    title = currentState.title,
                    category = currentState.category,
                    amount = currentState.convertedAmount ?: amount,
                    originalAmount = amount,
                    currency = currentState.currency,
                    baseCurrency = "USD",
                    date = currentState.date,
                    notes = currentState.notes
                )
                
                // Here you would save to repository/database
                // repository.saveTransaction(transactionData)
                
                _state.value = currentState.copy(isLoading = false)
                _effect.emit(AddTransactionContract.SideEffect.ShowSuccess("Transaction saved successfully"))
                _effect.emit(AddTransactionContract.SideEffect.NavigateToTransactions)
                
            } catch (e: Exception) {
                _state.value = currentState.copy(
                    isLoading = false,
                    error = "Failed to save transaction"
                )
                _effect.emit(AddTransactionContract.SideEffect.ShowError("Failed to save transaction"))
            }
        }
    }
}