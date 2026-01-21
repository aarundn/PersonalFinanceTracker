package com.example.personalfinancetracker.features.transaction.add_transaction

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Transaction
import com.example.domain.model.Type
import com.example.domain.usecase.AddTransactionUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class AddTransactionViewModel(
    private val addTransactionUseCase: AddTransactionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(
        AddTransactionContract.State(
            date = System.currentTimeMillis()
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
                _state.value = _state.value.copy(date = event.date.toLong())
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
                delay(1000)

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

        Log.d("AddTransactionViewModel1", "Saving transaction with data: $currentState")

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

                val transactionData = Transaction(
                    id = java.util.UUID.randomUUID().toString(),
                    userId = "A1", // TODO: Get actual user ID
                    amount = amount,
                    currency = currentState.currency,
                    categoryId = currentState.category, // Using name as ID for now based on selector
                    date = currentState.date,
                    notes = currentState.notes,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    type = if (currentState.isIncome) Type.INCOME else Type.EXPENSE,
                )

                Log.d("AddTransactionViewModel", "Attempting to save transaction: $transactionData")

                addTransactionUseCase(transactionData).onSuccess {
                    Log.d(
                        "AddTransactionViewModel2",
                        "Transaction saved successfully: $transactionData"
                    )
                    _state.value = currentState.copy(isLoading = false)
                    _effect.emit(AddTransactionContract.SideEffect.NavigateBack)
                }.onFailure {
                    _state.value = currentState.copy(
                        isLoading = false,
                        error = "Failed to save transaction"
                    )
                    Log.e("AddTransactionViewModel3", "Exception saving transaction ${it.message}")
                    _effect.emit(AddTransactionContract.SideEffect.ShowError("Failed to save transaction"))
                }


            } catch (e: Exception) {
                _state.value = currentState.copy(
                    isLoading = false,
                    error = "Failed to save transaction"
                )
                Log.e("AddTransactionViewModel3", "Exception saving transaction", e)
                _effect.emit(AddTransactionContract.SideEffect.ShowError("Failed to save transaction"))
            }
        }
    }
}