package com.example.personalfinancetracker.features.transaction.transactions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetTransactionsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionsViewModel(
    private val getTransactionsUseCase: GetTransactionsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(TransactionsContract.State())
    val state: StateFlow<TransactionsContract.State> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<TransactionsContract.SideEffect>()
    val effects: SharedFlow<TransactionsContract.SideEffect> = _effects.asSharedFlow()

    init {
        loadTransactions()
    }

    fun onEvent(event: TransactionsContract.Event) {
        when (event) {
            TransactionsContract.Event.LoadTransactions -> loadTransactions()
            is TransactionsContract.Event.OnTransactionClick -> {
                viewModelScope.launch {
                    _effects.emit(TransactionsContract.SideEffect.NavigateToTransactionDetails(event.transaction.id.toInt()))
                }
            }

            TransactionsContract.Event.OnAddTransactionClick -> {
                viewModelScope.launch {
                    _effects.emit(TransactionsContract.SideEffect.NavigateToAddTransaction)
                }
            }

            TransactionsContract.Event.OnRetry -> loadTransactions()
        }
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)

                // Get transactions from repository
                 getTransactionsUseCase().collect { transactions ->
                     Log.d("TransactionsViewModel", "Transactions: ${transactions.size}")
                    _state.update {  it.copy(
                        transactions = transactions,
                        isLoading = false,
                        error = null
                    )
                    }
                }

            } catch (_: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Failed to load transactions"
                )
                _effects.emit(TransactionsContract.SideEffect.ShowError("Failed to load transactions"))
            }
        }
    }
}