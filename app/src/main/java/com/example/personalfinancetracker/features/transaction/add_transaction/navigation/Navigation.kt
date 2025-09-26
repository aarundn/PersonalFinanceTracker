package com.example.personalfinancetracker.features.transaction.add_transaction.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.personalfinancetracker.features.transaction.add_transaction.AddTransactionRoute
import com.example.personalfinancetracker.features.transaction.navigation.TransactionRoutes

fun NavController.navigateToAddTransactionScreen() {
    navigate(TransactionRoutes.AddTransactionRoute)
}

fun NavGraphBuilder.addTransactionRoute(navController: NavController) {
    composable<TransactionRoutes.AddTransactionRoute> {
        AddTransactionRoute(navController = navController)
    }
}