package com.example.personalfinancetracker.features.home.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.conversion_rate.navigation.CurrencyConversionNavigator
import com.example.core.navigation.features.HomeFeature

class HomeFeatureImpl : HomeFeature {
    override fun homeRoute(): Any = HomeRoutes.HomeRoute


    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        modifier: Modifier
    ) {
        navGraphBuilder.homeRoute(
            navController = navController,
            currencyConversionNavigator = CurrencyConversionNavigator.Default
        )
    }
}