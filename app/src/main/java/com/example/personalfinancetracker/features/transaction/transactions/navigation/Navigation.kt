package com.example.personalfinancetracker.features.transaction.transactions.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.personalfinancetracker.features.transaction.navigation.TransactionRoutes

fun NavController.navigateToTransactionsScreen() {
    navigate(TransactionRoutes.TransactionsRoute)
}

fun NavGraphBuilder.transactionRoute(navController: NavController) {
    composable<TransactionRoutes.TransactionsRoute> {
        // todo transactions screen
    }
}