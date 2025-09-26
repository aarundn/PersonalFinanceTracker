package com.example.personalfinancetracker.features.transaction.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.core.navigation.features.TransactionFeature
import com.example.personalfinancetracker.features.transaction.add_transaction.navigation.addTransactionRoute
import com.example.personalfinancetracker.features.transaction.edit_transaction.navigation.editTransactionRoute
import com.example.personalfinancetracker.features.transaction.transactions.navigation.transactionRoute

class TransactionFeatureImpl: TransactionFeature {
    override fun transactionsRoute(): Any = TransactionRoutes.TransactionsRoute

    override fun addTransactionRoute(): Any = TransactionRoutes.AddTransactionRoute

    override fun editTransactionRoute(transactionId: String) = TransactionRoutes.EditTransactionRoute(transactionId)

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        modifier: Modifier
    ) {
        navGraphBuilder.transactionRoute(navController = navController, modifier = modifier)
        navGraphBuilder.addTransactionRoute(navController = navController)
        navGraphBuilder.editTransactionRoute(navController = navController)
    }

}