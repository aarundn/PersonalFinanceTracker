package com.example.personalfinancetracker.features.transaction.transactions

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.platform.LocalContext

@Composable
fun TransactionsRoute(
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToEditTransaction: (String) -> Unit,
    viewModel: TransactionsViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val transactionsUiState = viewModel.transactionsUiState.collectAsStateWithLifecycle().value
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is TransactionsSideEffect.NavigateToAddTransaction -> {
                    onNavigateToAddTransaction()
                }
                is TransactionsSideEffect.NavigateToTransactionDetails -> {
                    onNavigateToEditTransaction(effect.transactionId)
                }
                is TransactionsSideEffect.ShowError -> {
                    snackBarHostState.showSnackbar(effect.message.asString(context))
                }
            }
        }
    }

    TransactionsScreen(
        transactionsUiState = transactionsUiState,
        onEvent = viewModel::onEvent,
        snackBarHostState = snackBarHostState,
        modifier = modifier
    )
}
