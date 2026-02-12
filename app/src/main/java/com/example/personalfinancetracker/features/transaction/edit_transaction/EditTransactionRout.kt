package com.example.personalfinancetracker.features.transaction.edit_transaction

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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

                }
                is EditTransactionSideEffect.ShowSuccess -> {

                }
            }
        }
    }

    EditTransactionScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}
