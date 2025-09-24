package com.example.personalfinancetracker.features.transaction.edit_transaction.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.personalfinancetracker.features.transaction.navigation.TransactionRoutes

fun NavController.navigateToEditTransactionScreen(transactionId: String) {
    navigate(TransactionRoutes.EditTransactionRoute(transactionId = transactionId))
}

fun NavGraphBuilder.editTransactionRoute(navController: NavController) {
    composable<TransactionRoutes.EditTransactionRoute> { backStackEntry ->
        val transactionId =
            backStackEntry.toRoute<TransactionRoutes.EditTransactionRoute>().transactionId
        //todo add edit transaction screen
    }
}