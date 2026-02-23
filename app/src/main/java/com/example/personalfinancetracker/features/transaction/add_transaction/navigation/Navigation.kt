package com.example.personalfinancetracker.features.transaction.add_transaction.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.personalfinancetracker.features.transaction.add_transaction.AddTransactionRoute
import com.example.personalfinancetracker.features.transaction.navigation.TransactionRoutes

fun NavGraphBuilder.addTransactionRoute(
    onNavigateBack: () -> Unit
) {
    composable<TransactionRoutes.AddTransactionRoute> {
        AddTransactionRoute(
            onNavigateBack = onNavigateBack
        )
    }
}