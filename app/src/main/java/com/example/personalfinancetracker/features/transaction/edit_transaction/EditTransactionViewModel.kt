package com.example.personalfinancetracker.features.transaction.edit_transaction

import androidx.lifecycle.viewModelScope
import com.example.core.common.BaseTransactionUiEvent
import com.example.core.common.BaseTransactionViewModel
import com.example.domain.model.Type
import com.example.domain.repo.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class EditTransactionViewModel(
    private val repository: TransactionRepository
) : BaseTransactionViewModel<EditTransactionContract.State, BaseTransactionUiEvent, EditTransactionContract.SideEffect>() {

    override val _uiState = MutableStateFlow(EditTransactionContract.State())

    val sideEffect = uiSideEffect

    init {
        // Intentionally empty, load logic triggered by event or ID set
    }

    fun onEvent(event: EditTransactionContract.Event) {
        when (event) {
            is EditTransactionContract.Event.OnTransactionTypeChanged -> {
                setState { copy(isIncome = event.isIncome, category = "") }
                refreshCategories()
            }
            is EditTransactionContract.Event.OnTitleChanged -> setState { copy(title = event.title) }
            is EditTransactionContract.Event.OnCategoryChanged -> setState { copy(category = event.category) }
            is EditTransactionContract.Event.OnAmountChanged -> setState { copy(amount = event.amount) }
            is EditTransactionContract.Event.OnCurrencyChanged -> setState { copy(currency = event.currency) }
            is EditTransactionContract.Event.OnDateChanged -> setState { copy(date = event.date) }
            is EditTransactionContract.Event.OnNotesChanged -> setState { copy(notes = event.notes) }
            
            is EditTransactionContract.Event.OnLocationChanged -> setState { copy(location = event.location) }
            is EditTransactionContract.Event.OnPaymentMethodChanged -> setState { copy(paymentMethod = event.paymentMethod) }
            
            EditTransactionContract.Event.OnConvertCurrency -> convertCurrency()
            
            EditTransactionContract.Event.OnSave -> saveTransaction()
            EditTransactionContract.Event.OnCancel -> navigateBack()

            EditTransactionContract.Event.OnDelete -> {
                 viewModelScope.launch {
                    emitSideEffect(EditTransactionContract.SideEffect.ShowDeleteConfirmation(_uiState.value.transactionId))
                }
            }
            EditTransactionContract.Event.OnEdit -> setState { copy(isEditing = true) }
            EditTransactionContract.Event.OnLoadTransaction -> loadTransaction()
        }
    }

    private fun refreshCategories() {
        val categories = getCategoriesForType(_uiState.value.isIncome)
        setState { copy(categories = categories) }
    }

    private fun convertCurrency() {
        viewModelScope.launch {
            try {
                setState { copy(isConverting = true) }
                val result = calculateCurrencyConversion(_uiState.value.amount, _uiState.value.currency)
                setState { copy(convertedAmount = result, isConverting = false) }
            } catch (e: Exception) {
                setState { copy(isConverting = false, error = "Currency conversion failed") }
                showError("Currency conversion failed ${e.message}")
            }
        }
    }

    private fun loadTransaction() {
        setState { copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val transaction = repository.getTransactionById(_uiState.value.transactionId)

                transaction?.let { txn ->
                    val isIncome = txn.type == Type.INCOME
                    setState {
                        copy(
                            isLoading = false,
                            isIncome = isIncome,
                            category = txn.category,
                            amount = txn.amount.toString(),
                            currency = txn.currency,
                            date = txn.date,
                            notes = txn.notes ?: "",
                            categories = getCategoriesForType(isIncome)
                        )
                    }
                } ?: run {
                    setState { copy(isLoading = false) }
                }
            } catch (e: Exception) {
                setState { copy(isLoading = false, error = e.message) }
                showError(e.message ?: "Failed to load transaction")
            }
        }
    }

    private fun saveTransaction() {
        val currentState = _uiState.value

        // Validate required fields
        if (currentState.title.isBlank() || currentState.category.isBlank() || currentState.amount.isBlank()) {
            showError("Please fill in all required fields")
            return
        }

        setState { copy(isLoading = true) }

        viewModelScope.launch {
            try {
                // Simulate API call
                kotlinx.coroutines.delay(1000)

                // Here you would call your repository to update the transaction
                // repository.updateTransaction(createTransactionData())

                setState { copy(isLoading = false, isEditing = false) }
                emitSideEffect(EditTransactionContract.SideEffect.ShowSuccess("Transaction updated successfully"))
                emitSideEffect(EditTransactionContract.SideEffect.NavigateBack)

            } catch (e: Exception) {
                setState { copy(isLoading = false, error = e.message) }
                showError(e.message ?: "Failed to update transaction")
            }
        }
    }

    private fun navigateBack() {
         viewModelScope.launch {
            emitSideEffect(EditTransactionContract.SideEffect.NavigateBack)
        }
    }

    private fun showError(message: String) {
        viewModelScope.launch {
             emitSideEffect(EditTransactionContract.SideEffect.ShowError(message))
        }
    }
    
    fun setTransactionId(transactionId: String) {
        setState { copy(transactionId = transactionId) }
    }
}