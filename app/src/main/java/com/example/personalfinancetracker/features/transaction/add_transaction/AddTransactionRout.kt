package com.example.personalfinancetracker.features.transaction.add_transaction

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.personalfinancetracker.features.transaction.transactions.navigation.navigateToTransactionsScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddTransactionRoute(
    navController: NavController,
    viewModel: AddTransactionViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Handle side effects
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TransactionSideEffect.NavigateBack -> {
                    navController.popBackStack()
                }

                is TransactionSideEffect.NavigateToTransactions -> navController.navigateToTransactionsScreen()
                is TransactionSideEffect.ShowError -> {
                    // Show error message using your preferred method (Snackbar, Toast, etc.)
                }

                is TransactionSideEffect.ShowSuccess -> {
                    // Show success message using your preferred method (Snackbar, Toast, etc.)
                }
            }
        }
    }

    AddTransactionScreen(
        state = state,
        onEvent = viewModel::onEvent,
    )
}