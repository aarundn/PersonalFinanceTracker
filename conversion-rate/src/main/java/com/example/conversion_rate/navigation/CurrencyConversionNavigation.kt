package com.example.conversion_rate.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.conversion_rate.presentation.CurrencyConverterRoute


const val currencyConverterRoute = "currency_converter"

fun NavGraphBuilder.currencyConverterScreen(
    onNavigateBack: () -> Unit = {}
) {
    composable(route = currencyConverterRoute) {
        CurrencyConverterRoute(onNavigateBack)
    }
}

interface CurrencyConversionNavigator {
    fun navigateToCurrencyConverter(navController: NavController)

    object Default : CurrencyConversionNavigator {
        override fun navigateToCurrencyConverter(navController: NavController) {
            navController.navigate(currencyConverterRoute)
        }
    }
}