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
import java.util.UUID


class AddTransactionViewModel(
    private val addTransactionUseCase: AddTransactionUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(
        TransactionState(
            date = System.currentTimeMillis()
        )
    )
    val state: StateFlow<TransactionState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<TransactionSideEffect>()
    val effect: SharedFlow<TransactionSideEffect> = _effect.asSharedFlow()

    fun onEvent(event: TransactionEvent) {
        when (event) {
            is TransactionEvent.OnTransactionTypeChanged -> {
                _state.value = _state.value.copy(
                    isIncome = event.isIncome,
                    category = "" // Reset category when type changes
                )
            }

            is TransactionEvent.OnTitleChanged -> {
                _state.value = _state.value.copy(title = event.title)
            }

            is TransactionEvent.OnCategoryChanged -> {
                _state.value = _state.value.copy(category = event.category)
            }

            is TransactionEvent.OnAmountChanged -> {
                _state.value = _state.value.copy(amount = event.amount)
                // Trigger currency conversion if needed
                if (event.amount.isNotEmpty() && _state.value.currency != "USD") {
                    convertCurrency()
                }
            }

            is TransactionEvent.OnCurrencyChanged -> {
                _state.value = _state.value.copy(currency = event.currency)
                // Trigger currency conversion if amount exists
                if (_state.value.amount.isNotEmpty() && event.currency != "USD") {
                    convertCurrency()
                }
            }

            is TransactionEvent.OnDateChanged -> {
                _state.value = _state.value.copy(date = event.date.toLong())
            }

            is TransactionEvent.OnNotesChanged -> {
                _state.value = _state.value.copy(notes = event.notes)
            }

            TransactionEvent.OnConvertCurrency -> {
                convertCurrency()
            }

            TransactionEvent.OnSave -> {
                saveTransaction()
            }

            TransactionEvent.OnCancel -> {
                viewModelScope.launch {
                    _effect.emit(TransactionSideEffect.NavigateBack)
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
                _effect.emit(TransactionSideEffect.ShowError("Currency conversion failed"))
            }
        }
    }

    private fun saveTransaction() {
        val currentState = _state.value

        Log.d("AddTransactionViewModel1", "Saving transaction with data: $currentState")

        if (currentState.category.isEmpty()) {
            viewModelScope.launch {
                _effect.emit(TransactionSideEffect.ShowError("Category is required"))
            }
            return
        }

        if (currentState.amount.isEmpty()) {
            viewModelScope.launch {
                _effect.emit(TransactionSideEffect.ShowError("Amount is required"))
            }
            return
        }

        val amount = currentState.amount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            viewModelScope.launch {
                _effect.emit(TransactionSideEffect.ShowError("Please enter a valid amount"))
            }
            return
        }

        _state.value = currentState.copy(isLoading = true)

        viewModelScope.launch {
            try {

                val transactionData = Transaction(
                    id = UUID.randomUUID().toString(),
                    userId = "A1", // TODO: Get actual user ID
                    amount = amount,
                    currency = currentState.currency,
                    category = currentState.category,
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
                    _effect.emit(TransactionSideEffect.NavigateBack)
                }.onFailure {
                    _state.value = currentState.copy(
                        isLoading = false,
                        error = "Failed to save transaction"
                    )
                    Log.e("AddTransactionViewModel3", "Exception saving transaction ${it.message}")
                    _effect.emit(TransactionSideEffect.ShowError("Failed to save transaction"))
                }


            } catch (e: Exception) {
                _state.value = currentState.copy(
                    isLoading = false,
                    error = "Failed to save transaction"
                )
                Log.e("AddTransactionViewModel3", "Exception saving transaction", e)
                _effect.emit(TransactionSideEffect.ShowError("Failed to save transaction"))
            }
        }
    }
}