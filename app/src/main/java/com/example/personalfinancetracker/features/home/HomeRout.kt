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
import androidx.compose.ui.platform.LocalContext

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeRoute(
    onNavigateToCurrency: () -> Unit,
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToSettings:  () -> Unit,
    onNavigateToAddBudget:  () -> Unit,
    modifier: Modifier,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is HomeSideEffect.NavigateAddTransaction -> onNavigateToAddTransaction()
                is HomeSideEffect.NavigateTransfer -> { /* No-op per spec */ }
                is HomeSideEffect.NavigateSettings -> onNavigateToSettings()
                is HomeSideEffect.NavigateTransactionDetails -> { /* Navigate to details */ }
                is HomeSideEffect.ShowMessage -> snackBarHostState.showSnackbar(effect.message.asString(context))
                else -> {}
            }
        }
    }

    HomeScreen(
        modifier = modifier,
        homeUiState = homeUiState,
        onEvent = viewModel::onEvent,
    )
}