package com.example.personalfinancetracker.features.transaction.add_transaction

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.platform.LocalContext

@Composable
fun AddTransactionRoute(
    onNavigateBack: () -> Unit,
    onNavigateToAddBudget: () -> Unit,
    viewModel: AddTransactionViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {

                is AddTransactionSideEffect.NavigateToAddBudget -> onNavigateToAddBudget()
                is AddTransactionSideEffect.NavigateBack -> onNavigateBack()

                is AddTransactionSideEffect.NavigateToTransactions -> onNavigateBack()

                is AddTransactionSideEffect.ShowError -> snackBarHostState.showSnackbar(effect.message.asString(context))

                is AddTransactionSideEffect.ShowSuccess -> snackBarHostState.showSnackbar(effect.message.asString(context))
            }
        }
    }

    AddTransactionScreen(
        state = state,
        onEvent = viewModel::onEvent,
        snackBarHostState = snackBarHostState,
    )
}