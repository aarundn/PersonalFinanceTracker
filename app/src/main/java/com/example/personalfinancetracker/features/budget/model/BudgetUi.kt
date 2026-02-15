package com.example.personalfinancetracker.features.budget.model

import androidx.compose.runtime.Immutable
import com.example.core.model.Category
import com.example.core.model.DefaultCategories
import com.example.core.model.DefaultCurrencies
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
    val currentCategory: Category = DefaultCategories.fromId(category) ?: DefaultCategories.OTHER,
    val formattedCreatedDate: String = parseDateString(createdAt),
    val currencySymbol: String = DefaultCurrencies.fromId(currency)?.symbol ?: currency,
) {
    val percentage: Float get() = if (amount == 0.0) 0f else (spent / amount).toFloat().coerceIn(0f, 1f)
    val  remaining: Double get() = (amount - spent).coerceAtLeast(0.0)
    val overBudget: Double get() = (spent - amount).coerceAtLeast(0.0)
    val isOverBudget: Boolean get() = spent > amount
    val isWarning: Boolean get() = percentage > 0.8f && !isOverBudget
}
