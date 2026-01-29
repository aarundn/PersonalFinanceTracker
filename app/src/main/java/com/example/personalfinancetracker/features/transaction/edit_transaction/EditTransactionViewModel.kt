package com.example.personalfinancetracker.features.transaction.edit_transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.core.common.BaseViewModel
import com.example.core.common.MVIUiEvent
import com.example.core.common.TransactionOperations
import com.example.domain.model.Type
import com.example.domain.repo.TransactionRepository
import com.example.domain.usecase.ValidateTransactionInputsUseCase
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditTransactionViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: TransactionRepository,
    private val validateInputsUseCase: ValidateTransactionInputsUseCase,
    private val transactionOps: TransactionOperations
) : BaseViewModel<EditTransactionState, MVIUiEvent, EditTransactionSideEffect>() {

    companion object {
        const val TRANSACTION_ID_KEY = "transactionId"
    }

    private val transactionId: String = savedStateHandle.get<String>(TRANSACTION_ID_KEY) ?: ""

    override fun createInitialState() = EditTransactionState()


    init {
        loadTransaction()
    }

    private fun loadTransaction() {
        if (transactionId.isEmpty()) {
            updateState { copy(isLoading = false, error = "Invalid transaction ID") }
            return
        }

        updateState { copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val transaction = repository.getTransactionById(transactionId)

                transaction?.let { txn ->
                    val isIncome = txn.type == Type.INCOME
                    updateState {
                        copy(
                            transactionId = transactionId,
                            isLoading = false,
                            isIncome = isIncome,
                            title = txn.category,
                            category = txn.category,
                            amount = txn.amount.toString(),
                            currency = txn.currency,
                            date = txn.date,
                            notes = txn.notes ?: "",
                            categories = transactionOps.getCategoriesForType(isIncome)
                        )
                    }
                } ?: run {
                    updateState { copy(isLoading = false, error = "Transaction not found") }
                }
            } catch (e: Exception) {
                updateState { copy(isLoading = false, error = e.message) }
                showError(e.message ?: "Failed to load transaction")
            }
        }
    }
    override fun handleEvent(event: MVIUiEvent) {
        when (event) {
            is EditTransactionEvent.OnTransactionTypeChanged -> {
                updateState { copy(isIncome = event.isIncome, category = "") }
                refreshCategories()
            }

            is EditTransactionEvent.OnTitleChanged -> updateState { copy(title = event.title) }
            is EditTransactionEvent.OnCategoryChanged -> updateState { copy(category = event.category) }
            is EditTransactionEvent.OnAmountChanged -> updateState { copy(amount = event.amount) }
            is EditTransactionEvent.OnCurrencyChanged -> updateState { copy(currency = event.currency) }
            is EditTransactionEvent.OnDateChanged -> updateState { copy(date = event.date) }
            is EditTransactionEvent.OnNotesChanged -> updateState { copy(notes = event.notes) }

            is EditTransactionEvent.OnLocationChanged -> updateState { copy(location = event.location) }
            is EditTransactionEvent.OnPaymentMethodChanged -> updateState { copy(paymentMethod = event.paymentMethod) }


            EditTransactionEvent.OnSave -> saveTransaction()
            EditTransactionEvent.OnCancel -> navigateBack()

            EditTransactionEvent.OnDelete -> {
                viewModelScope.launch {
                    triggerSideEffect(EditTransactionSideEffect.ShowDeleteConfirmation(_uiState.value.transactionId))
                }
            }

            EditTransactionEvent.OnEdit -> updateState { copy(isEditing = true) }

            // No longer needed - loading happens automatically when state is collected
            EditTransactionEvent.OnLoadTransaction -> { }
        }
    }

    fun onEvent(event: EditTransactionEvent) {

    }

    /**
     * Update internal state - this is for UI interactions after initial load.
     */
    private fun updateState(update: EditTransactionState.() -> EditTransactionState) {
        _uiState.update { it.update() }
    }

    private fun refreshCategories() {
        val categories = transactionOps.getCategoriesForType(_uiState.value.isIncome)
        updateState { copy(categories = categories) }
    }


    private fun saveTransaction() {
        val currentState = _uiState.value

        // Use validation use case
        val validationError = validateInputsUseCase.getValidationError(
            currentState.category,
            currentState.amount
        )
        if (validationError != null) {
            showError(validationError)
            return
        }

        updateState { copy(isLoading = true) }

        viewModelScope.launch {
            try {
                // TODO: Call repository.updateTransaction(...)

                updateState { copy(isLoading = false, isEditing = false) }
                triggerSideEffect(EditTransactionSideEffect.ShowSuccess("Transaction updated successfully"))
                triggerSideEffect(EditTransactionSideEffect.NavigateBack)

            } catch (e: Exception) {
                updateState { copy(isLoading = false, error = e.message) }
                showError(e.message ?: "Failed to update transaction")
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            triggerSideEffect(EditTransactionSideEffect.NavigateBack)
        }
    }

    private fun showError(message: String) {
        viewModelScope.launch {
            triggerSideEffect(EditTransactionSideEffect.ShowError(message))
        }
    }




}