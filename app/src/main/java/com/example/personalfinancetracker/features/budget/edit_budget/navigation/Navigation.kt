package com.example.personalfinancetracker.features.budget.edit_budget.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.personalfinancetracker.features.budget.edit_budget.EditBudgetRoute
import com.example.personalfinancetracker.features.budget.navigation.BudgetRoutes

fun NavController.navigateToEditBudgetScreen(budgetId: String) {
    navigate(BudgetRoutes.EditBudgetRoute(budgetId = budgetId))
}

fun NavGraphBuilder.editBudgetRoute(navController: NavController) {
    composable<BudgetRoutes.EditBudgetRoute> {
        EditBudgetRoute(
            onNavigateBack = { navController.popBackStack() }
        )
    }
}