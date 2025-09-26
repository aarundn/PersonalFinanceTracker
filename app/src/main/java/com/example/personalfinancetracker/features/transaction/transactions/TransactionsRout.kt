package com.example.personalfinancetracker.features.transaction.transactions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.personalfinancetracker.features.transaction.add_transaction.navigation.navigateToAddTransactionScreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TransactionsRoute(
    navController: NavController,
    viewModel: TransactionsViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                TransactionsContract.SideEffect.NavigateToAddTransaction -> {
                    navController.navigateToAddTransactionScreen()
                }
                is TransactionsContract.SideEffect.NavigateToTransactionDetails -> {
                    // TODO: Implement navigation to transaction details
                }
                is TransactionsContract.SideEffect.ShowError -> {
                    // Show error message using your preferred method (Snackbar, Toast, etc.)
                }
            }
        }
    }

    TransactionsScreen(
        state = state,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}
