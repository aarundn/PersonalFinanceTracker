package com.example.personalfinancetracker.features.budget.add_budget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddBudgetRoute(
    onNavigateBack: () -> Unit,
    viewModel: AddBudgetViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.sideEffects.collectLatest { sideEffect ->
            when (sideEffect) {
                AddBudgetContract.SideEffect.NavigateBack -> onNavigateBack()
                is AddBudgetContract.SideEffect.ShowError -> {
                    // Hook into snackbar host when available
                }
                is AddBudgetContract.SideEffect.ShowSuccess -> {
                    // Hook into snackbar host when available
                }
            }
        }
    }

    AddBudgetScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}
