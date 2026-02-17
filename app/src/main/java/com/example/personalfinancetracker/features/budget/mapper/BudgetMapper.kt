package com.example.personalfinancetracker.features.budget.mapper

import com.example.domain.model.Budget
import com.example.personalfinancetracker.features.budget.model.BudgetUi

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
        spent = spent
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
    )
}

fun List<BudgetUi>.toBudget(): List<Budget> = map { it.toBudget() }




