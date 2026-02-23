package com.example.personalfinancetracker.features.budget.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.core.navigation.AppRoutes
import com.example.core.navigation.features.BudgetFeature
import com.example.personalfinancetracker.features.budget.add_budget.navigation.addBudgetRoute
import com.example.personalfinancetracker.features.budget.budgets.navigation.budgetRoute
import com.example.personalfinancetracker.features.budget.edit_budget.navigation.editBudgetRoute

class BudgetFeatureImpl : BudgetFeature {
    override fun addBudgetRoute(): AppRoutes = BudgetRoutes.AddBudgetRoute

    override fun budgetsRoute(): AppRoutes = BudgetRoutes.BudgetsRoute

    override fun editBudgetRoute(budgetId: String): AppRoutes = BudgetRoutes.EditBudgetRoute(budgetId)

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        modifier: Modifier,
    ) {
        navGraphBuilder.budgetRoute(
            onNavigateToAddBudget = { navController.navigate(addBudgetRoute()) },
            onNavigateToEditBudget = { budgetId -> navController.navigate(editBudgetRoute(budgetId)) },
            modifier = modifier
        )
        navGraphBuilder.addBudgetRoute(onNavigateBack = { navController.popBackStack() })
        navGraphBuilder.editBudgetRoute(onNavigateBack = { navController.popBackStack() })
    }
}