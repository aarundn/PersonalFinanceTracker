package com.example.personalfinancetracker.features.budget.navigation

import kotlinx.serialization.Serializable

sealed interface BudgetRoutes{
    @Serializable
    data object BudgetsRoute : BudgetRoutes

    @Serializable
    data object AddBudgetRoute : BudgetRoutes

    @Serializable
    data class EditBudgetRoute(val budgetId: String) : BudgetRoutes
}