package com.example.personalfinancetracker.features.home.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.conversion_rate.navigation.CurrencyConversionNavigator
import com.example.core.navigation.AppRoutes
import com.example.core.navigation.features.HomeFeature
import com.example.core.navigation.features.SettingsFeature
import com.example.core.navigation.features.TransactionFeature

class HomeFeatureImpl(
    private val transactionsFeature: TransactionFeature,
    private val settingsFeature: SettingsFeature
) : HomeFeature {
    override fun homeRoute(): AppRoutes = HomeRoutes.HomeRoute


    @RequiresApi(Build.VERSION_CODES.O)
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        modifier: Modifier
    ) {

        navGraphBuilder.homeRoute(
            modifier = modifier,
            onNavigateToAddTransaction = { navController.navigate(transactionsFeature.addTransactionRoute()) },
            onNavigateToSettings = { navController.navigate(settingsFeature.settingsRoute()) },
            onNavigateToCurrency = {
                    CurrencyConversionNavigator.Default.navigateToCurrencyConverter(navController = navController)
            },
        )
    }
}