package com.example.personalfinancetracker.features.budget.model

import androidx.compose.runtime.Immutable
import com.example.core.utils.parseDateString

@Immutable
data class BudgetUi(
    val id: String,
    val userId: String,
    val category: String,
    val amount: Double,
    val spent: Double,
    val currency: String,
    val period: String,
    val notes: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val formattedCreatedDate: String = parseDateString(createdAt),
    val periodLabel: String = period.replaceFirstChar { it.uppercase() },
    val currencySymbol: String = currency.substringBefore(")").substringAfter("(")
) {
    val percentage: Float get() = if (amount == 0.0) 0f else (spent / amount).toFloat().coerceIn(0f, 1f)
    val  remaining: Double get() = (amount - spent).coerceAtLeast(0.0)
    val overBudget: Double get() = (spent - amount).coerceAtLeast(0.0)
    val isOverBudget: Boolean get() = spent > amount
    val isWarning: Boolean get() = percentage > 0.8f && !isOverBudget
}
