package com.example.personalfinancetracker.features.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.personalfinancetracker.features.settings.SettingsRoute

fun NavGraphBuilder.settingsRoute(
    onNavigateBack: () -> Unit,
) {
    composable<SettingsRoutes.SettingsRoute> {
        SettingsRoute(
            onNavigateBack = onNavigateBack,
        )
    }
}
