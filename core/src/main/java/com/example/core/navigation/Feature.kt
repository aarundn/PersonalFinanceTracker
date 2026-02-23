package com.example.core.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

interface Feature {
    fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        modifier: Modifier = Modifier
    )
}

fun NavGraphBuilder.register(
    feature: Feature,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    feature.registerGraph(this, navController, modifier)
}

interface AppRoutes