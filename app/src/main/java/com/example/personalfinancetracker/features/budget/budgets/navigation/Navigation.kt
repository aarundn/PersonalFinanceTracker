package com.example.personalfinancetracker.features.budget.budgets.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.personalfinancetracker.features.budget.budgets.BudgetRoute
import com.example.personalfinancetracker.features.budget.navigation.BudgetRoutes

fun NavGraphBuilder.budgetRoute(
    onNavigateToAddBudget: () -> Unit,
    onNavigateToEditBudget: (String) -> Unit,
    modifier: Modifier
) {
    composable<BudgetRoutes.BudgetsRoute> {
        BudgetRoute(
            onNavigateToAddBudget = onNavigateToAddBudget,
            onNavigateToEditBudget = onNavigateToEditBudget,
            modifier = modifier
        )
    }
}