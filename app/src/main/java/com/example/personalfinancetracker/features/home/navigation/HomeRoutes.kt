package com.example.personalfinancetracker.features.home.navigation

import com.example.core.navigation.AppRoutes
import kotlinx.serialization.Serializable

sealed interface HomeRoutes: AppRoutes {
    @Serializable
    data object HomeRoute: HomeRoutes
}