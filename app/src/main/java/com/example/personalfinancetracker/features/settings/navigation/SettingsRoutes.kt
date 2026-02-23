package com.example.personalfinancetracker.features.settings.navigation

import com.example.core.navigation.AppRoutes
import kotlinx.serialization.Serializable

sealed interface SettingsRoutes: AppRoutes {
    @Serializable
    data object SettingsRoute : SettingsRoutes
}
