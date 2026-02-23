package com.example.personalfinancetracker.features.settings.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.personalfinancetracker.features.settings.SettingsRoute

fun NavController.navigateToSettingsScreen() {
    navigate(SettingsRoutes.SettingsRoute)
}

fun NavGraphBuilder.settingsRoute(
    navController: NavController,
) {
    composable<SettingsRoutes.SettingsRoute> {
        SettingsRoute(
            navController = navController,
        )
    }
}
