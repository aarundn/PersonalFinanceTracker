package com.example.personalfinancetracker.features.transaction.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.transaction_usecases.GetTransactionsUseCase
import com.example.personalfinancetracker.features.transaction.mapper.toTransactionUi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransactionsViewModel(
    getTransactionsUseCase: GetTransactionsUseCase,
) : ViewModel() {

    private val _sideEffect = MutableSharedFlow<TransactionsSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    val transactionsUiState: StateFlow<TransactionsUiState> =
        getTransactionsUseCase()
            .map<_, TransactionsUiState> { transactions ->
                TransactionsUiState.Success(transactions.toTransactionUi())
            }
            .onStart { emit(TransactionsUiState.Loading) }
            .catch { e ->
                emit(TransactionsUiState.Error(e.message ?: "Failed to load transactions"))
                _sideEffect.emit(
                    TransactionsSideEffect.ShowError(
                        e.message ?: "Failed to load transactions"
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = TransactionsUiState.Loading,
            )

    fun onEvent(event: TransactionsEvent) {
        viewModelScope.launch {
            when (event) {
                is TransactionsEvent.OnTransactionClick -> {
                    _sideEffect.emit(
                        TransactionsSideEffect.NavigateToTransactionDetails(event.transaction.id)
                    )
                }

                TransactionsEvent.OnAddTransactionClick -> {
                    _sideEffect.emit(TransactionsSideEffect.NavigateToAddTransaction)
                }
            }
        }
    }
}