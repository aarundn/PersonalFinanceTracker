package com.example.personalfinancetracker.features.budget.navigation

import com.example.core.navigation.AppRoutes
import kotlinx.serialization.Serializable

sealed interface BudgetRoutes: AppRoutes{
    @Serializable
    data object BudgetsRoute : BudgetRoutes

    @Serializable
    data object AddBudgetRoute : BudgetRoutes

    @Serializable
    data class EditBudgetRoute(val budgetId: String) : BudgetRoutes
}