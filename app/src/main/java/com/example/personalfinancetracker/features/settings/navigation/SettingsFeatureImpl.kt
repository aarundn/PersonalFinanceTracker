package com.example.personalfinancetracker.features.settings.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.core.navigation.features.SettingsFeature

class SettingsFeatureImpl : SettingsFeature {
    override fun settingsRoute(): Any = SettingsRoutes.SettingsRoute

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        modifier: Modifier
    ) {
        navGraphBuilder.settingsRoute(navController = navController)
    }
}
