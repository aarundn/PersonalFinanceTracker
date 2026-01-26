package com.example.personalfinancetracker.features.transaction.edit_transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Type
import com.example.domain.repo.TransactionRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class EditTransactionViewModel (
    private val repository: TransactionRepository
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

                val transaction = repository.getTransactionById(state.value.transactionId)

                transaction?.let { transaction ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isIncome = transaction.type == Type.INCOME,
                            category = transaction.category,
                            amount = transaction.amount.toString(),
                            currency = transaction.currency,
                            date = transaction.date,
                            notes = transaction.notes ?: "",
                        )
                    }
                }
                
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




    fun setTransactionId(transactionId: String) {
        _state.value = _state.value.copy(transactionId = transactionId)
    }
}