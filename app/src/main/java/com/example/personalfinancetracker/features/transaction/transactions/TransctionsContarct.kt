package com.example.personalfinancetracker.features.transaction.transactions

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * UDF contract for Transactions feature
 */
object TransactionsContract {
    @Immutable
    data class State(
        val transactions: List<com.example.domain.model.Transaction> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    @Immutable
    data class Transaction(
        val id: Int,
        val title: String,
        val category: String,
        val amount: Double,
        val date: String,
        val notes: String? = null,
        val location: String? = null,
        val paymentMethod: String,
        val icon: ImageVector,
        val iconTint: Color
    )

    sealed interface Event {
        data object LoadTransactions : Event
        data class OnTransactionClick(val transaction: com.example.domain.model.Transaction) : Event
        data object OnAddTransactionClick : Event
        data object OnRetry : Event
    }

    sealed interface SideEffect {
        data object NavigateToAddTransaction : SideEffect
        data class NavigateToTransactionDetails(val transactionId: Int) : SideEffect
        data class ShowError(val message: String) : SideEffect
    }
}

interface TransactionsRepository {
    fun getTransactions(): List<TransactionsContract.Transaction>
}

class InMemoryTransactionsRepository : TransactionsRepository {
    override fun getTransactions(): List<TransactionsContract.Transaction> {
        return emptyList() // Will be populated by ViewModel
    }
}
