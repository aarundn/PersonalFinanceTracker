package com.example.core.navigation.features

import com.example.core.navigation.AppRoutes
import com.example.core.navigation.Feature

interface SettingsFeature : Feature {
    fun settingsRoute(): AppRoutes
}
