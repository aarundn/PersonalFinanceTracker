package com.example.personalfinancetracker.features.budget.edit_budget.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.personalfinancetracker.features.budget.navigation.BudgetRoutes

fun NavController.navigateToEditBudgetScreen(budgetId: String) {
    navigate(BudgetRoutes.EditBudgetRoute(budgetId = budgetId))
}

fun NavGraphBuilder.editBudgetRoute(navController: NavController) {
    composable<BudgetRoutes.EditBudgetRoute> { backStackEntry ->
        val budgetId = backStackEntry.toRoute<BudgetRoutes.EditBudgetRoute>().budgetId
        //todo add edit budget screen
    }
}