package com.example.personalfinancetracker.features.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.personalfinancetracker.features.budget.budgets.navigation.navigateToBudgetScreen
import com.example.personalfinancetracker.features.transaction.add_transaction.navigation.navigateToAddTransactionScreen
import com.example.personalfinancetracker.features.transaction.transactions.navigation.navigateToTransactionsScreen
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoute(

    navController: NavController,
    onNavigateToCurrency: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                HomeContract.SideEffect.NavigateBudgets -> navController.navigateToBudgetScreen()
                HomeContract.SideEffect.NavigateTransactions -> navController.navigateToTransactionsScreen()
                HomeContract.SideEffect.NavigateAddExpense -> navController.navigateToAddTransactionScreen()
                HomeContract.SideEffect.NavigateAddIncome -> navController.navigateToAddTransactionScreen()
                HomeContract.SideEffect.NavigateCurrency -> onNavigateToCurrency()
                is HomeContract.SideEffect.ShowMessage -> { /* Snackbar later */ }
            }
        }
    }

    HomeScreen(state = state, onEvent = viewModel::onEvent, modifier)
}