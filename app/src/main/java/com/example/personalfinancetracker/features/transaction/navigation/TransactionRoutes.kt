package com.example.personalfinancetracker.features.transaction.navigation

import com.example.core.navigation.AppRoutes
import kotlinx.serialization.Serializable

sealed interface TransactionRoutes: AppRoutes {
    @Serializable
    data object TransactionsRoute : TransactionRoutes

    @Serializable
    data object AddTransactionRoute : TransactionRoutes

    @Serializable
    data class EditTransactionRoute(val transactionId: String) : TransactionRoutes
}