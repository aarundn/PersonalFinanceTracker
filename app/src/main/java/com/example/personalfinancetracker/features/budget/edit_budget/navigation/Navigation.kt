package com.example.personalfinancetracker.features.budget.edit_budget.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.personalfinancetracker.features.budget.edit_budget.EditBudgetRoute
import com.example.personalfinancetracker.features.budget.navigation.BudgetRoutes

fun NavGraphBuilder.editBudgetRoute(
    onNavigateBack: () -> Unit
) {
    composable<BudgetRoutes.EditBudgetRoute> {
        EditBudgetRoute(
            onNavigateBack = onNavigateBack
        )
    }
}