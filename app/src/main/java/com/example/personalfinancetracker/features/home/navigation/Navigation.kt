package com.example.personalfinancetracker.features.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.conversion_rate.navigation.CurrencyConversionNavigator
import com.example.personalfinancetracker.features.home.HomeRoute

fun NavController.navigateToHomeScreen() {
    navigate(HomeRoutes.HomeRoute)
}

fun NavGraphBuilder.homeRoute(
    navController: NavController,
    currencyConversionNavigator: CurrencyConversionNavigator
) {
    composable<HomeRoutes.HomeRoute> {
        HomeRoute(navController = navController, onNavigateToCurrency = {
            currencyConversionNavigator.navigateToCurrencyConverter(navController = navController)
        })
    }
}