package com.example.personalfinancetracker.features.transaction.add_transaction

import androidx.lifecycle.viewModelScope
import com.example.core.common.BaseViewModel
import com.example.core.common.MVIUiEvent
import com.example.core.model.DefaultCategories
import com.example.domain.ValidationResult
import com.example.domain.model.Type
import com.example.domain.usecase.AddTransactionUseCase
import com.example.domain.usecase.ValidateTransactionInputsUseCase
import com.example.personalfinancetracker.features.transaction.mapper.toTransaction
import com.example.personalfinancetracker.features.transaction.model.TransactionUi
import kotlinx.coroutines.launch
import java.util.UUID

class AddTransactionViewModel(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val validateInputsUseCase: ValidateTransactionInputsUseCase
) : BaseViewModel<AddTransactionState, MVIUiEvent, AddTransactionSideEffect>() {

    init {
        refreshCategories()
    }

    private fun refreshCategories() {
        val categories = DefaultCategories.getCategories(_uiState.value.isIncome)
        setState { copy(categories = categories) }
    }

    override fun createInitialState() = AddTransactionState()

    override fun handleEvent(event: MVIUiEvent) {

        when (event) {
            is AddTransactionEvent.OnTransactionTypeChanged -> {
                setState { copy(isIncome = event.isIncome, selectedCategory = null) }
                refreshCategories()
            }
            is AddTransactionEvent.OnCategoryChanged -> setState { copy(selectedCategory = event.category) }
            is AddTransactionEvent.OnAmountChanged -> setState { copy(amount = event.amount) }
            is AddTransactionEvent.OnCurrencyChanged -> setState { copy(currency = event.currency) }
            is AddTransactionEvent.OnDateChanged -> setState { copy(date = event.date) }
            is AddTransactionEvent.OnNotesChanged -> setState { copy(notes = event.notes) }
            AddTransactionEvent.OnSave -> saveTransaction()
            AddTransactionEvent.OnCancel -> navigateBack()
        }
    }


    private fun navigateBack() {
        viewModelScope.launch {
            triggerSideEffect(AddTransactionSideEffect.NavigateBack)
        }
    }

    private fun saveTransaction() {
        val currentState = _uiState.value

        val validationResult = validateInputsUseCase(
            category = currentState.selectedCategory?.id ?: "",
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
                val transactionData = TransactionUi(
                    id = UUID.randomUUID().toString(),
                    userId = "A1", // TODO: Get actual user ID
                    amount = currentState.amount.toDoubleOrNull() ?: 0.0,
                    currency = currentState.currency,
                    category = currentState.selectedCategory?.id ?: "",
                    date = currentState.date,
                    notes = currentState.notes,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    type = if (currentState.isIncome) Type.INCOME else Type.EXPENSE,
                ).toTransaction()

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