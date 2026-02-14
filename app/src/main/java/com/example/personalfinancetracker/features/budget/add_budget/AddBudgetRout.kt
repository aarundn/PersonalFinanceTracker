package com.example.personalfinancetracker.features.budget.add_budget

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddBudgetRoute(
    onNavigateBack: () -> Unit,
    viewModel: AddBudgetViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                AddBudgetSideEffect.NavigateBack -> onNavigateBack()
                is AddBudgetSideEffect.ShowError -> snackBarHostState.showSnackbar(effect.message)
                is AddBudgetSideEffect.ShowSuccess -> snackBarHostState.showSnackbar(effect.message)
            }
        }
    }

    AddBudgetScreen(
        state = state,
        onEvent = viewModel::onEvent,
        snackBarHostState = snackBarHostState
    )
}
