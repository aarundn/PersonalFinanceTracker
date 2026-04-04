package com.example.personalfinancetracker.features.budget.mapper

import com.example.domain.model.Budget
import com.example.personalfinancetracker.features.budget.model.BudgetUi

import com.example.core.model.BudgetDisplayData

fun Budget.toBudgetUi(spent: Double): BudgetUi {
    return BudgetUi(
        id = id,
        userId = userId,
        category = category,
        amount = amount,
        currency = currency,
        period = period,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt,
        spent = spent,
        syncStatusEnum = syncStatus
    )
}

fun BudgetUi.toDisplayData(): BudgetDisplayData {
    return BudgetDisplayData(
        iconTint = currentCategory.color,
        iconBackground = currentCategory.color.copy(alpha = 0.12f),
        icon = currentCategory.icon,
        categoryName = currentCategory.nameResId,
        currencySymbol = currencySymbol,
        spent = spent,
        amount = amount,
        isOverBudget = isOverBudget,
        isWarning = isWarning,
        overBudget = overBudget,
        remaining = remaining
    )
}

fun BudgetUi.toBudget(): Budget {
    return Budget(
        id = id,
        userId = userId,
        category = category,
        amount = amount,
        currency = currency,
        period = period,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt,
        syncStatus = syncStatusEnum
    )
}




