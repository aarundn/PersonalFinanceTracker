package com.example.personalfinancetracker.features.transaction.edit_transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.core.common.BaseViewModel
import com.example.core.common.MVIUiEvent
import com.example.core.model.DefaultCategories
import com.example.domain.ValidationResult
import com.example.domain.model.Type
import com.example.domain.repo.TransactionRepository
import com.example.domain.usecase.transaction_usecases.DeleteTransactionUseCase
import com.example.domain.usecase.transaction_usecases.UpdateTransactionUseCase
import com.example.domain.usecase.ValidateTransactionInputsUseCase
import com.example.personalfinancetracker.features.transaction.mapper.toTransaction
import com.example.personalfinancetracker.features.transaction.mapper.toTransactionUi
import com.example.personalfinancetracker.features.transaction.model.TransactionUi
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditTransactionViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: TransactionRepository,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val validateInputsUseCase: ValidateTransactionInputsUseCase
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
                val transactionUi = repository.getTransactionById(transactionId)?.toTransactionUi()
                transactionUi?.let { txn ->

                    val categories = DefaultCategories.getCategories(txn.isIncome)
                    val selectedCategory = DefaultCategories.fromId(txn.category)
                    
                    updateState {
                        copy(
                            transactionId = transactionId,
                            isLoading = false,
                            isIncome = txn.isIncome,
                            selectedCategory = selectedCategory,
                            amount = txn.amount.toString(),
                            currency = txn.currency,
                            date = txn.date,
                            notes = txn.notes ?: "",
                            categories = categories
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
                updateState { copy(isIncome = event.isIncome, selectedCategory = null) }
                refreshCategories()
            }

            is EditTransactionEvent.OnCategoryChanged -> updateState { copy(selectedCategory = event.category) }
            is EditTransactionEvent.OnAmountChanged -> updateState { copy(amount = event.amount) }
            is EditTransactionEvent.OnCurrencyChanged -> updateState { copy(currency = event.currency) }
            is EditTransactionEvent.OnDateChanged -> updateState { copy(date = event.date) }
            is EditTransactionEvent.OnNotesChanged -> updateState { copy(notes = event.notes) }
            EditTransactionEvent.OnSave -> saveTransaction()
            EditTransactionEvent.OnCancel -> navigateBack()

            EditTransactionEvent.OnDelete -> updateState { copy(showDeleteConfirmation = true) }
            EditTransactionEvent.OnDismissDelete -> updateState { copy(showDeleteConfirmation = false) }
            EditTransactionEvent.OnConfirmDelete -> deleteTransaction()
            EditTransactionEvent.OnEdit -> updateState { copy(isEditing = true) }
        }
    }


    private fun updateState(update: EditTransactionState.() -> EditTransactionState) {
        _uiState.update { it.update() }
    }

    private fun refreshCategories() {
        val categories = DefaultCategories.getCategories(_uiState.value.isIncome)
        updateState { copy(categories = categories) }
    }


    private fun saveTransaction() {
        val currentState = uiState.value

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

        updateState { copy(isLoading = true) }

        viewModelScope.launch {
            try {

                val transactionData = TransactionUi(
                    id = transactionId,
                    userId = "A1", // TODO: Get actual user ID
                    amount = currentState.amount.toDoubleOrNull() ?: 0.0,
                    currency = currentState.currency,
                    category = currentState.selectedCategory?.id ?: "",
                    date = System.currentTimeMillis(),
                    notes = currentState.notes,
                    createdAt = currentState.date,
                    updatedAt = System.currentTimeMillis(),
                    type = if (currentState.isIncome) Type.INCOME else Type.EXPENSE,
                ).toTransaction()

                updateTransactionUseCase(transactionData).onSuccess {
                    updateState { copy(isLoading = false, isEditing = false) }
                    triggerSideEffect(EditTransactionSideEffect.ShowSuccess("Transaction updated successfully"))
                    triggerSideEffect(EditTransactionSideEffect.NavigateBack)
                }.onFailure { e ->
                    updateState { copy(isLoading = false, error = "Failed to update transaction") }
                    triggerSideEffect(EditTransactionSideEffect.ShowError("Failed to update transaction: ${e.message}"))
                }

            } catch (e: Exception) {
                updateState { copy(isLoading = false, error = e.message) }
                showError(e.message ?: "Failed to update transaction")
            }
        }
    }

    private fun deleteTransaction() {
        updateState { copy(showDeleteConfirmation = false, isLoading = true) }

        viewModelScope.launch {
            deleteTransactionUseCase(transactionId).onSuccess {
                updateState { copy(isLoading = false) }
                triggerSideEffect(EditTransactionSideEffect.ShowSuccess("Transaction deleted successfully"))
                triggerSideEffect(EditTransactionSideEffect.NavigateBack)
            }.onFailure { e ->
                updateState { copy(isLoading = false, error = "Failed to delete transaction") }
                triggerSideEffect(EditTransactionSideEffect.ShowError("Failed to delete transaction: ${e.message}"))
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
