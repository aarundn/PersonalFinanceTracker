package com.example.personalfinancetracker.features.budget.model

data class BudgetInsightsUi(
    val daysElapsed: Int,
    val daysTotal: Int,
    val averageDailySpend: Double,
    val projectedTotal: Double,
    val isProjectedOverBudget: Boolean
)
