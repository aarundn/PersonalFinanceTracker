package com.example.personalfinancetracker.features.budget.budgets

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun BudgetRoute(
    modifier: Modifier = Modifier,
    onNavigateToAddBudget: () -> Unit,
    onNavigateToEditBudget: (String) -> Unit,
    viewModel: BudgetViewModel = koinViewModel(),

) {
    val budgetsUiState by viewModel.budgetsUiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                BudgetsSideEffect.NavigateToAddBudget -> {
                    onNavigateToAddBudget()
                }
                is BudgetsSideEffect.NavigateToBudgetDetails -> {
                    onNavigateToEditBudget(effect.budgetId)
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