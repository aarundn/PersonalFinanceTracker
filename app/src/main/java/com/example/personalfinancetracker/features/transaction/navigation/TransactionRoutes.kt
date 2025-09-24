package com.example.personalfinancetracker.features.transaction.navigation

import kotlinx.serialization.Serializable

sealed interface TransactionRoutes {
    @Serializable
    data object TransactionsRoute : TransactionRoutes

    @Serializable
    data object AddTransactionRoute : TransactionRoutes

    @Serializable
    data class EditTransactionRoute(val transactionId: String) : TransactionRoutes
}