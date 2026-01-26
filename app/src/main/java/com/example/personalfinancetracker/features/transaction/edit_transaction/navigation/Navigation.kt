package com.example.personalfinancetracker.features.transaction.edit_transaction.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.personalfinancetracker.features.transaction.edit_transaction.EditTransactionRoute
import com.example.personalfinancetracker.features.transaction.navigation.TransactionRoutes

fun NavController.navigateToEditTransaction(transactionId: String) {
    navigate(TransactionRoutes.EditTransactionRoute(transactionId))
}

fun NavGraphBuilder.editTransactionRoute(navController: NavController) {
    composable<TransactionRoutes.EditTransactionRoute> { backStackEntry ->
        val transactionId =
            backStackEntry.arguments?.getString("transactionId") ?: ""

        EditTransactionRoute(
            transactionId = transactionId,
            onNavigateBack = { navController.popBackStack() },
            onNavigateToTransactions = {
                navController.navigate(TransactionRoutes.TransactionsRoute) {
                    popUpTo(TransactionRoutes.TransactionsRoute) {
                        inclusive = true
                    }
                }
            }
        )
    }
}