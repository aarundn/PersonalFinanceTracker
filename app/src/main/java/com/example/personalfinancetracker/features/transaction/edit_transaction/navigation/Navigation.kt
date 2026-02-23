package com.example.personalfinancetracker.features.transaction.edit_transaction.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.personalfinancetracker.features.transaction.edit_transaction.EditTransactionRoute
import com.example.personalfinancetracker.features.transaction.navigation.TransactionRoutes

fun NavGraphBuilder.editTransactionRoute(
    onNavigateBack: () -> Unit
) {
    composable<TransactionRoutes.EditTransactionRoute> {
        EditTransactionRoute(
            onNavigateBack = onNavigateBack
        )
    }
}