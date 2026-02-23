package com.example.personalfinancetracker.features.budget.add_budget.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.personalfinancetracker.features.budget.add_budget.AddBudgetRoute
import com.example.personalfinancetracker.features.budget.navigation.BudgetRoutes

fun NavGraphBuilder.addBudgetRoute(
    onNavigateBack: () -> Unit
) {
    composable<BudgetRoutes.AddBudgetRoute> {
        AddBudgetRoute(
            onNavigateBack = onNavigateBack
        )
    }
}