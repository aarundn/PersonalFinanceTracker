package com.example.personalfinancetracker.features.settings.navigation

import kotlinx.serialization.Serializable

sealed interface SettingsRoutes {
    @Serializable
    data object SettingsRoute : SettingsRoutes
}
