package com.example.personalfinancetracker.features.budget.budgets.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.personalfinancetracker.features.budget.budgets.BudgetRoute
import com.example.personalfinancetracker.features.budget.navigation.BudgetRoutes

fun NavController.navigateToBudgetScreen() {
    navigate(BudgetRoutes.BudgetsRoute)
}
fun NavGraphBuilder.budgetRoute(navController: NavController, modifier: Modifier) {
    composable<BudgetRoutes.BudgetsRoute> {
        BudgetRoute(
            navController = navController, modifier = modifier
        )
    }
}