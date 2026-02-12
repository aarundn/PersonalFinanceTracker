package com.example.personalfinancetracker.features.transaction.edit_transaction

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Suppress("ParamsComparedByRef")
@Composable
fun EditTransactionRoute(
    onNavigateBack: () -> Unit,
    onNavigateToTransactions: () -> Unit,
    viewModel: EditTransactionViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

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
                    snackBarHostState.showSnackbar(sideEffect.message)
                }
                is EditTransactionSideEffect.ShowSuccess -> {
                    snackBarHostState.showSnackbar(sideEffect.message)
                }
            }
        }
    }

    EditTransactionScreen(
        state = state,
        onEvent = viewModel::onEvent,
        snackBarHostState = snackBarHostState
    )
}
