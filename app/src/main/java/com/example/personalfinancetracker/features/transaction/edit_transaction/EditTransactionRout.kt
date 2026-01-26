package com.example.personalfinancetracker.features.transaction.edit_transaction

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditTransactionRoute(
    transactionId: String,
    onNavigateBack: () -> Unit,
    onNavigateToTransactions: () -> Unit,
    viewModel: EditTransactionViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Handle side effects
    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is EditTransactionContract.SideEffect.NavigateBack -> {
                    onNavigateBack()
                }
                is EditTransactionContract.SideEffect.NavigateToTransactions -> {
                    onNavigateToTransactions()
                }
                is EditTransactionContract.SideEffect.ShowError -> {
                    // Handle error display (could be a snackbar or dialog)
                    // For now, we'll just ignore it as we don't have error handling UI
                }
                is EditTransactionContract.SideEffect.ShowSuccess -> {
                    // Handle success display (could be a snackbar)
                    // For now, we'll just ignore it as we don't have success handling UI
                }
                is EditTransactionContract.SideEffect.ShowDeleteConfirmation -> {
                    // Handle delete confirmation dialog
                    // For now, we'll just ignore it as we don't have dialog UI
                }
            }
        }
    }

    // Set transaction ID when route is first composed
    LaunchedEffect(transactionId) {
        viewModel.setTransactionId(transactionId)
    }

    EditTransactionScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}