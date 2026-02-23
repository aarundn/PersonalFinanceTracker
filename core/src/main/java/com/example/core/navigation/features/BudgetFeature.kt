package com.example.core.navigation.features

import com.example.core.navigation.AppRoutes
import com.example.core.navigation.Feature

interface BudgetFeature : Feature {
    fun addBudgetRoute(): AppRoutes
    fun budgetsRoute(): AppRoutes
    fun editBudgetRoute(budgetId: String): AppRoutes
}