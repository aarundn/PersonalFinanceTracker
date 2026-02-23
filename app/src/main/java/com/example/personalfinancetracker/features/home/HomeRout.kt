package com.example.personalfinancetracker.features.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.personalfinancetracker.features.budget.budgets.navigation.navigateToBudgetScreen
import com.example.personalfinancetracker.features.settings.navigation.navigateToSettingsScreen
import com.example.personalfinancetracker.features.transaction.add_transaction.navigation.navigateToAddTransactionScreen
import com.example.personalfinancetracker.features.transaction.transactions.navigation.navigateToTransactionsScreen
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
    onNavigateToCurrency: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                HomeContract.SideEffect.NavigateBudgets -> navController.navigateToBudgetScreen()
                HomeContract.SideEffect.NavigateTransactions -> navController.navigateToTransactionsScreen()
                HomeContract.SideEffect.NavigateAddExpense -> navController.navigateToAddTransactionScreen()
                HomeContract.SideEffect.NavigateAddIncome -> navController.navigateToAddTransactionScreen()
                HomeContract.SideEffect.NavigateCurrency -> onNavigateToCurrency()
                HomeContract.SideEffect.NavigateSettings -> navController.navigateToSettingsScreen()
                is HomeContract.SideEffect.ShowMessage -> snackBarHostState.showSnackbar(effect.message)
            }
        }
    }

    HomeScreen(
        homeUiState = homeUiState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}