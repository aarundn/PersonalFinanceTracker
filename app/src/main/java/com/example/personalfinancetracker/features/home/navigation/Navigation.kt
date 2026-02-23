package com.example.personalfinancetracker.features.home.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.personalfinancetracker.features.home.HomeRoute



@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.homeRoute(
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToSettings:  () -> Unit,
    onNavigateToCurrency: () -> Unit,
    modifier: Modifier,

    ) {
    composable<HomeRoutes.HomeRoute> {
        HomeRoute(
            onNavigateToCurrency = onNavigateToCurrency,
            onNavigateToAddTransaction = onNavigateToAddTransaction,
            onNavigateToSettings = onNavigateToSettings,
            modifier = modifier
        )
    }
}