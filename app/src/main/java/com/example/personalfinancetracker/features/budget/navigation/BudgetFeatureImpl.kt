package com.example.personalfinancetracker.features.budget.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.core.navigation.features.BudgetFeature
import com.example.personalfinancetracker.features.budget.add_budget.navigation.addBudgetRoute
import com.example.personalfinancetracker.features.budget.budgets.navigation.budgetRoute
import com.example.personalfinancetracker.features.budget.edit_budget.navigation.editBudgetRoute

class BudgetFeatureImpl : BudgetFeature {
    override fun addBudgetRoute(): Any = BudgetRoutes.AddBudgetRoute

    override fun budgetsRoute(): Any = BudgetRoutes.BudgetsRoute

    override fun editBudgetRoute(budgetId: String) = BudgetRoutes.EditBudgetRoute(budgetId)

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        modifier: Modifier,
    ) {
        navGraphBuilder.budgetRoute(navController = navController, modifier = modifier)
        navGraphBuilder.addBudgetRoute(navController = navController)
        navGraphBuilder.editBudgetRoute(navController = navController)
    }
}