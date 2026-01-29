package com.example.personalfinancetracker.features.transaction.add_transaction

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.personalfinancetracker.features.transaction.transactions.navigation.navigateToTransactionsScreen
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddTransactionRoute(
    navController: NavController,
    viewModel: AddTransactionViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val snackBarHostState = remember { SnackbarHostState() }


    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is AddTransactionSideEffect.NavigateBack -> navController.popBackStack()

                is AddTransactionSideEffect.NavigateToTransactions ->
                    navController.navigateToTransactionsScreen()

                is AddTransactionSideEffect.ShowError -> snackBarHostState.showSnackbar(effect.message)

                is AddTransactionSideEffect.ShowSuccess -> snackBarHostState.showSnackbar(effect.message)

            }
        }
    }

    AddTransactionScreen(
        state = state,
        onEvent = viewModel::onEvent,
        snackBarHostState = snackBarHostState
    )
}