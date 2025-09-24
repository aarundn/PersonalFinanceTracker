package com.example.personalfinancetracker.features.budget.add_budget.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.personalfinancetracker.features.budget.navigation.BudgetRoutes

fun NavController.navigateToAddBudgetScreen() {
    navigate(BudgetRoutes.AddBudgetRoute)
}
fun NavGraphBuilder.addBudgetRoute(navController: NavController) {
    composable<BudgetRoutes.AddBudgetRoute> {
        //todo add add budget screen
    }
}