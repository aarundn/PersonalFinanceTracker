package com.example.personalfinancetracker.features.budget.edit_budget

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditBudgetRoute(
    onNavigateBack: () -> Unit,
    viewModel: EditBudgetViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }


    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is EditBudgetSideEffect.NavigateBack -> onNavigateBack()
                is EditBudgetSideEffect.ShowError -> {
                    launch {
                        snackBarHostState.showSnackbar(sideEffect.message)
                    }
                }
                is EditBudgetSideEffect.ShowSuccess -> {
                    launch {
                        snackBarHostState.showSnackbar(sideEffect.message)
                    }
                }
            }
        }
    }

    EditBudgetScreen(
        state = state,
        onEvent = viewModel::onEvent,
        snackBarHostState = snackBarHostState
    )
}
