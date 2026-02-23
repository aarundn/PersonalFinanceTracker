package com.example.personalfinancetracker.features.transaction.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.core.navigation.AppRoutes
import com.example.core.navigation.features.TransactionFeature
import com.example.personalfinancetracker.features.transaction.add_transaction.navigation.addTransactionRoute
import com.example.personalfinancetracker.features.transaction.edit_transaction.navigation.editTransactionRoute
import com.example.personalfinancetracker.features.transaction.transactions.navigation.transactionRoute

class TransactionFeatureImpl : TransactionFeature {
    override fun transactionsRoute(): AppRoutes = TransactionRoutes.TransactionsRoute

    override fun addTransactionRoute(): AppRoutes = TransactionRoutes.AddTransactionRoute

    override fun editTransactionRoute(transactionId: String): AppRoutes =
        TransactionRoutes.EditTransactionRoute(transactionId)

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        modifier: Modifier
    ) {
        navGraphBuilder.transactionRoute(
            onNavigateToAddTransaction = { navController.navigate(addTransactionRoute()) },
            onNavigateToEditTransaction = { transactionId ->
                navController.navigate(editTransactionRoute(transactionId))
            },
            modifier = modifier
        )
        navGraphBuilder.addTransactionRoute(onNavigateBack = { navController.popBackStack() })
        navGraphBuilder.editTransactionRoute(onNavigateBack = { navController.popBackStack() })
    }
}