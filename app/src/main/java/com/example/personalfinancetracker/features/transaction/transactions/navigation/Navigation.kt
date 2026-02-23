package com.example.personalfinancetracker.features.transaction.transactions.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.personalfinancetracker.features.transaction.navigation.TransactionRoutes
import com.example.personalfinancetracker.features.transaction.transactions.TransactionsRoute

fun NavGraphBuilder.transactionRoute(
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToEditTransaction: (String) -> Unit,
    modifier: Modifier
) {
    composable<TransactionRoutes.TransactionsRoute> {
       TransactionsRoute(
           onNavigateToAddTransaction = onNavigateToAddTransaction,
           onNavigateToEditTransaction = onNavigateToEditTransaction,
           modifier = modifier
       )
    }
}