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
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeRoute(
    onNavigateToCurrency: () -> Unit,
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToSettings:  () -> Unit,
    modifier: Modifier,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                HomeContract.SideEffect.NavigateAddExpense -> onNavigateToAddTransaction()
                HomeContract.SideEffect.NavigateAddIncome -> onNavigateToAddTransaction()
                HomeContract.SideEffect.NavigateCurrency -> onNavigateToCurrency()
                HomeContract.SideEffect.NavigateSettings -> onNavigateToSettings()
                is HomeContract.SideEffect.ShowMessage -> snackBarHostState.showSnackbar(effect.message)
            }
        }
    }

    HomeScreen(
        modifier = modifier,
        homeUiState = homeUiState,
        onEvent = viewModel::onEvent,
    )
}