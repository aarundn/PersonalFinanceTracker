package com.example.personalfinancetracker.features.transaction.transactions

import androidx.compose.runtime.Immutable
import com.example.personalfinancetracker.features.transaction.model.TransactionUi


@Immutable
sealed interface TransactionsUiState {
    data object Loading : TransactionsUiState
    data class Success(val transactions: List<TransactionUi>) : TransactionsUiState
    data class Error(val message: String) : TransactionsUiState
}


sealed interface TransactionsEvent {
    data class OnTransactionClick(val transaction: TransactionUi) : TransactionsEvent
    data object OnAddTransactionClick : TransactionsEvent
}

sealed interface TransactionsSideEffect {
    data object NavigateToAddTransaction : TransactionsSideEffect
    data class NavigateToTransactionDetails(val transactionId: String) : TransactionsSideEffect
    data class ShowError(val message: String) : TransactionsSideEffect
}
