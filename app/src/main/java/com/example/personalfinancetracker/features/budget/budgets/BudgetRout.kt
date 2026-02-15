package com.example.personalfinancetracker.features.budget.budgets

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.personalfinancetracker.features.budget.add_budget.navigation.navigateToAddBudgetScreen
import com.example.personalfinancetracker.features.budget.edit_budget.navigation.navigateToEditBudgetScreen
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun BudgetRoute(
    navController: NavController,
    viewModel: BudgetViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val budgetsUiState by viewModel.budgetsUiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                BudgetsSideEffect.NavigateToAddBudget -> {
                    navController.navigateToAddBudgetScreen()
                }
                is BudgetsSideEffect.NavigateToBudgetDetails -> {
                    navController.navigateToEditBudgetScreen(effect.budgetId)
                }
                is BudgetsSideEffect.ShowError -> {
                    snackBarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    BudgetScreen(
        budgetsUiState = budgetsUiState,
        onEvent = viewModel::onEvent,
        snackBarHostState = snackBarHostState,
        modifier = modifier
    )
}