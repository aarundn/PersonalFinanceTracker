package com.example.personalfinancetracker.features.budget.common

import androidx.compose.runtime.Immutable

@Immutable
data class BudgetPeriodOption(
    val id: String,
    val label: String,
    val days: Int
)

object BudgetPeriodOptions {
    val Weekly = BudgetPeriodOption(id = "weekly", label = "Weekly", days = 7)
    val Monthly = BudgetPeriodOption(id = "monthly", label = "Monthly", days = 30)
    val Quarterly = BudgetPeriodOption(id = "quarterly", label = "Quarterly", days = 90)
    val Yearly = BudgetPeriodOption(id = "yearly", label = "Yearly", days = 365)

    val all = listOf(Weekly, Monthly, Quarterly, Yearly)
    val default = Monthly

    fun findById(id: String): BudgetPeriodOption = all.find { it.id == id } ?: default
}
