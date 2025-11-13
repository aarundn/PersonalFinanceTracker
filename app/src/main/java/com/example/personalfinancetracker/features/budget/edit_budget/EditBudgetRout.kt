package com.example.personalfinancetracker.features.budget.edit_budget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun EditBudgetRoute(
    budgetId: Int,
    onNavigateBack: () -> Unit,
    viewModel: EditBudgetViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.sideEffects.collect { sideEffect ->
            when (sideEffect) {
                is EditBudgetContract.SideEffect.NavigateBack -> onNavigateBack()
                is EditBudgetContract.SideEffect.ShowError -> {
                    // Hook up to snackbar host when available
                }
                is EditBudgetContract.SideEffect.ShowSuccess -> {
                    // Hook up to snackbar host when available
                }
            }
        }
    }

    LaunchedEffect(budgetId) {
        viewModel.setBudgetId(budgetId)
    }

    EditBudgetScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}
