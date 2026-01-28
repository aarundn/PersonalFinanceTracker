package com.example.personalfinancetracker.features.transaction.add_transaction

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.core.common.BaseTransactionUiEvent
import com.example.core.common.BaseTransactionUiSideEffect
import com.example.core.common.BaseTransactionViewModel
import com.example.domain.model.Transaction
import com.example.domain.model.Type
import com.example.domain.usecase.AddTransactionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AddTransactionViewModel(
    private val addTransactionUseCase: AddTransactionUseCase,
) : BaseTransactionViewModel<TransactionState, BaseTransactionUiEvent, BaseTransactionUiSideEffect>() {

    override val _uiState = MutableStateFlow(TransactionState())

    val effect = uiSideEffect

    init {
        refreshCategories()
    }

    private fun refreshCategories() {
        val categories = getCategoriesForType(_uiState.value.isIncome)
        setState { copy(categories = categories) }
    }

    fun onEvent(event: TransactionEvent) {
        when (event) {
            is TransactionEvent.OnTransactionTypeChanged -> {
                setState { copy(isIncome = event.isIncome, category = "") }
                refreshCategories()
            }
            is TransactionEvent.OnCategoryChanged -> setState { copy(category = event.category) }
            is TransactionEvent.OnAmountChanged -> {
                setState { copy(amount = event.amount) }
            }
            is TransactionEvent.OnCurrencyChanged -> {
                setState { copy(currency = event.currency) }
            }
            is TransactionEvent.OnDateChanged -> setState { copy(date = event.date) }
            is TransactionEvent.OnNotesChanged -> setState { copy(notes = event.notes) }
            TransactionEvent.OnSave -> saveTransaction()
            TransactionEvent.OnCancel -> navigateBack()
        }
    }


    private fun navigateBack() {
        viewModelScope.launch {
            emitSideEffect(TransactionSideEffect.NavigateBack)
        }
    }

    private fun saveTransaction() {
        val currentState = _uiState.value

        Log.d("AddTransactionViewModel1", "Saving transaction with data: $currentState")

        if (currentState.category.isEmpty()) {
            showError("Category is required")
            return
        }

        if (currentState.amount.isEmpty()) {
            showError("Amount is required")
            return
        }

        val amount = currentState.amount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            showError("Please enter a valid amount")
            return
        }

        setState { copy(isLoading = true) }

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

                addTransactionUseCase(transactionData).onSuccess {
                    setState { copy(isLoading = false) }
                    emitSideEffect(TransactionSideEffect.NavigateBack)
                    emitSideEffect(TransactionSideEffect.ShowSuccess("Transaction saved successfully"))

                }.onFailure { e ->
                    setState { copy(isLoading = false, error = "Failed to save transaction") }
                    emitSideEffect(TransactionSideEffect.ShowError("Failed to save transaction ${e.message}"))
                }

            } catch (e: Exception) {
                setState { copy(isLoading = false, error = "Failed to save transaction") }
                emitSideEffect(TransactionSideEffect.ShowError("Failed to save transaction ${e.message}"))
            }
        }
    }

    private fun showError(message: String) {
        viewModelScope.launch {
            emitSideEffect(TransactionSideEffect.ShowError(message))
        }
    }
}