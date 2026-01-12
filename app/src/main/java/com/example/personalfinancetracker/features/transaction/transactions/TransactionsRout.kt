package com.example.personalfinancetracker.features.transaction.transactions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import androidx.navigation.NavController
import com.example.personalfinancetracker.features.transaction.add_transaction.navigation.navigateToAddTransactionScreen
import com.example.personalfinancetracker.features.transaction.edit_transaction.navigation.navigateToEditTransaction
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun TransactionsRoute(
    navController: NavController,
    viewModel: TransactionsViewModel = koinViewModel(),
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
                    navController.navigateToEditTransaction(effect.transactionId)
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
