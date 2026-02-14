package com.example.personalfinancetracker.features.budget.mapper

import com.example.domain.model.Budget
import com.example.domain.model.Transaction
import com.example.domain.model.Type
import com.example.personalfinancetracker.features.budget.model.BudgetUi
import kotlin.collections.filter

fun Budget.toBudgetUi(transactions: List<Transaction>): BudgetUi {

    val periodByDays = when (period) {
        "weekly" -> 7
        "monthly" -> 30
        "quarterly" -> 90
        "yearly" -> 365
        else -> 30
    }
    val periodMillis = periodByDays * 24 * 60 * 60 * 1000L
    val periodEnd = this.createdAt + periodMillis
    val spent = transactions
        .filter { it.category == this.category
                && it.currency == this.currency }
        .filter { it.type == Type.EXPENSE }
        .filter { it.date in this.createdAt..periodEnd }
        .sumOf { it.amount }

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

fun List<Budget>.toBudgetUi(transaction: List<Transaction>): List<BudgetUi> =
    map { it.toBudgetUi(transaction) }

fun List<BudgetUi>.toBudget(): List<Budget> = map { it.toBudget() }
