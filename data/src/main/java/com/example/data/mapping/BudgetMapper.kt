package com.example.data.mapping

import com.example.data.local.model.BudgetEntity
import com.example.domain.model.Budget

fun BudgetEntity.toDomain(): Budget {
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

fun Budget.toEntity(): BudgetEntity {
    return BudgetEntity(
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

fun List<BudgetEntity>.toDomain(): List<Budget> = map { it.toDomain() }
fun List<Budget>.toEntity(): List<BudgetEntity> = map { it.toEntity() }
