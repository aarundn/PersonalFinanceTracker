package com.example.personalfinancetracker.features.transaction.add_transaction

import androidx.lifecycle.viewModelScope
import com.example.core.common.BaseViewModel
import com.example.core.common.MVIUiEvent
import com.example.core.model.Categories
import com.example.domain.ValidationResult
import com.example.domain.model.Transaction
import com.example.domain.model.Type
import com.example.domain.usecase.AddTransactionUseCase
import com.example.domain.usecase.ValidateTransactionInputsUseCase
import kotlinx.coroutines.launch
import java.util.UUID

class AddTransactionViewModel(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val validateInputsUseCase: ValidateTransactionInputsUseCase
) : BaseViewModel<AddTransactionState, MVIUiEvent, AddTransactionSideEffect>() {



    init {
        refreshCategories()
    }

    override fun createInitialState() = AddTransactionState()

    override fun handleEvent(event: MVIUiEvent) {

        when (event) {
            is AddTransactionEvent.OnTransactionTypeChanged -> {
                setState { copy(isIncome = event.isIncome, category = "") }
                refreshCategories()
            }

            is AddTransactionEvent.OnTitleChanged -> setState { copy(title = event.title) }
            is AddTransactionEvent.OnCategoryChanged -> setState { copy(category = event.category) }
            is AddTransactionEvent.OnAmountChanged -> setState { copy(amount = event.amount) }
            is AddTransactionEvent.OnCurrencyChanged -> setState { copy(currency = event.currency) }
            is AddTransactionEvent.OnDateChanged -> setState { copy(date = event.date) }
            is AddTransactionEvent.OnNotesChanged -> setState { copy(notes = event.notes) }
            AddTransactionEvent.OnSave -> saveTransaction()
            AddTransactionEvent.OnCancel -> navigateBack()
        }
    }

    private fun refreshCategories() {
        val categories = Categories.forType(if (_uiState.value.isIncome) Type.INCOME else Type.EXPENSE)
        setState { copy(categories = categories) }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            triggerSideEffect(AddTransactionSideEffect.NavigateBack)
        }
    }

    private fun saveTransaction() {
        val currentState = _uiState.value

        val validationResult = validateInputsUseCase(
            category = currentState.category,
            amount = currentState.amount,
            currency = currentState.currency,
            date = currentState.date
        )
        
        if (validationResult is ValidationResult.Error) {
            showError(validationResult.message)
            return
        }


        setState { copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val transactionData = Transaction(
                    id = UUID.randomUUID().toString(),
                    userId = "A1", // TODO: Get actual user ID
                    amount = currentState.amount.toDoubleOrNull() ?: 0.0,
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
                    triggerSideEffect(AddTransactionSideEffect.NavigateBack)
                    triggerSideEffect(AddTransactionSideEffect.ShowSuccess("Transaction saved successfully"))
                }.onFailure { e ->
                    setState { copy(isLoading = false, error = "Failed to save transaction") }
                    triggerSideEffect(AddTransactionSideEffect.ShowError("Failed to save transaction: ${e.message}"))
                }

            } catch (e: Exception) {
                setState { copy(isLoading = false, error = "Failed to save transaction") }
                triggerSideEffect(AddTransactionSideEffect.ShowError("Failed to save transaction: ${e.message}"))
            }
        }
    }

    private fun showError(message: String) {
        viewModelScope.launch {
            triggerSideEffect(AddTransactionSideEffect.ShowError(message))
        }
    }

}