package com.example.personalfinancetracker.features.transaction.transactions

import androidx.compose.runtime.Immutable
import com.example.personalfinancetracker.features.transaction.model.TransactionUi

/**
 * UDF contract for Transactions feature
 */
object TransactionsContract {
    @Immutable
    data class State(
        val transactions: List<TransactionUi> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )


    sealed interface Event {
        data object LoadTransactions : Event
        data class OnTransactionClick(val transaction: TransactionUi) : Event
        data object OnAddTransactionClick : Event
        data object OnRetry : Event
    }

    sealed interface SideEffect {
        data object NavigateToAddTransaction : SideEffect
        data class NavigateToTransactionDetails(val transactionId: String) : SideEffect
        data class ShowError(val message: String) : SideEffect
    }
}

