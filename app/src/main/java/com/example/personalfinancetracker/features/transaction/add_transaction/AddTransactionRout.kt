package com.example.personalfinancetracker.features.transaction.add_transaction

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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

    val snackBarHostState = remember { SnackbarHostState() }


    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TransactionSideEffect.NavigateBack -> navController.popBackStack()

                is TransactionSideEffect.NavigateToTransactions ->
                    navController.navigateToTransactionsScreen()

                is TransactionSideEffect.ShowError -> snackBarHostState.showSnackbar(effect.message)

                is TransactionSideEffect.ShowSuccess -> snackBarHostState.showSnackbar(effect.message)

            }
        }
    }

    AddTransactionScreen(
        state = state,
        onEvent = viewModel::onEvent,
        snackBarHostState = snackBarHostState
    )
}