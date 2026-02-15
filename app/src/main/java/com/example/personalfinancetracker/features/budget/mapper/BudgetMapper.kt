package com.example.personalfinancetracker.features.budget.mapper

import com.example.domain.model.Budget
import com.example.domain.model.Transaction
import com.example.domain.model.Type
import com.example.personalfinancetracker.features.budget.model.BudgetUi
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlin.collections.filter

fun Budget.toBudgetUi(transactions: List<Transaction>): BudgetUi {


    val periodEnd = this.getPeriodEnd()
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


fun Budget.getPeriodEnd(): Long {

    val timeZone = TimeZone.currentSystemDefault()
    val startDate = Instant.fromEpochMilliseconds(createdAt)
    val endDate = when (period.lowercase()) {
        "weekly" -> startDate.plus(1, DateTimeUnit.WEEK, timeZone)
        "monthly" -> startDate.plus(1, DateTimeUnit.MONTH, timeZone)
        "quarterly" -> startDate.plus(3, DateTimeUnit.MONTH, timeZone)
        "yearly" -> startDate.plus(1, DateTimeUnit.YEAR, timeZone)
        else -> startDate.plus(1, DateTimeUnit.MONTH, timeZone)
    }

    return endDate.toEpochMilliseconds()
}