package com.example.personalfinancetracker.features.home.navigation

import kotlinx.serialization.Serializable

sealed interface HomeRoutes {
    @Serializable
    data object HomeRoute: HomeRoutes
}