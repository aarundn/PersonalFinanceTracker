package com.example.personalfinancetracker.features.transaction.edit_transaction

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditTransactionRoute(
    onNavigateBack: () -> Unit,
    onNavigateToTransactions: () -> Unit,
    viewModel: EditTransactionViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle side effects
    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is EditTransactionSideEffect.NavigateBack -> {
                    onNavigateBack()
                }
                is EditTransactionSideEffect.NavigateToTransactions -> {
                    onNavigateToTransactions()
                }
                is EditTransactionSideEffect.ShowError -> {
                    // Handle error display (could be a snackbar or dialog)
                }
                is EditTransactionSideEffect.ShowSuccess -> {
                    // Handle success display (could be a snackbar)
                }
                is EditTransactionSideEffect.ShowDeleteConfirmation -> {
                    // Handle delete confirmation dialog
                }
            }
        }
    }

    // Transaction ID is now retrieved from SavedStateHandle - no LaunchedEffect needed

    EditTransactionScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}