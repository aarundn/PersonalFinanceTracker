package com.example.personalfinancetracker.features.budget.budgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.personalfinancetracker.features.budget.add_budget.navigation.navigateToAddBudgetScreen
import com.example.personalfinancetracker.features.budget.edit_budget.navigation.navigateToEditBudgetScreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun BudgetRoute(
    navController: NavController,
    viewModel: BudgetViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                BudgetContract.SideEffect.NavigateToAddBudget -> {
                    navController.navigateToAddBudgetScreen()
                }
                is BudgetContract.SideEffect.NavigateToBudgetDetails -> {
                    navController.navigateToEditBudgetScreen(effect.budgetId.toString())
                }
                is BudgetContract.SideEffect.ShowError -> {
                    // Show error message using your preferred method (Snackbar, Toast, etc.)
                }
            }
        }
    }

    BudgetScreen(
        state = state,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}