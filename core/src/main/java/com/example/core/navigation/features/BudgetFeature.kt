package com.example.core.navigation.features

import com.example.core.navigation.Feature

interface BudgetFeature : Feature {
    fun addBudgetRoute(): Any
    fun budgetsRoute(): Any
    fun editBudgetRoute(budgetId: String): Any
}