package com.example.personalfinancetracker.features.transaction.transactions.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.personalfinancetracker.features.transaction.add_transaction.navigation.navigateToAddTransactionScreen
import com.example.personalfinancetracker.features.transaction.edit_transaction.navigation.navigateToEditTransaction
import com.example.personalfinancetracker.features.transaction.navigation.TransactionRoutes
import com.example.personalfinancetracker.features.transaction.transactions.TransactionsRoute

fun NavController.navigateToTransactionsScreen() {
    navigate(TransactionRoutes.TransactionsRoute)
}

fun NavGraphBuilder.transactionRoute(navController: NavController, modifier: Modifier) {
    composable<TransactionRoutes.TransactionsRoute> {
       TransactionsRoute(
           onNavigateToAddTransaction = {navController.navigateToAddTransactionScreen()},
           onNavigateToEditTransaction = {transactionId -> navController.navigateToEditTransaction(transactionId)},
           modifier = modifier)
    }
}