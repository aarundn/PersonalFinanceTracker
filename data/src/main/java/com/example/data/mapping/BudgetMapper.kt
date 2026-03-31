package com.example.data.mapping

import com.example.data.local.model.BudgetEntity
import com.example.data.remote.model.BudgetDto
import com.example.domain.model.Budget

fun BudgetEntity.toDto(): BudgetDto {
    return BudgetDto(
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

fun BudgetDto.toEntity(): BudgetEntity {
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

fun List<BudgetDto>.toLocal(): List<BudgetEntity> = map { it.toEntity() }
fun List<BudgetEntity>.toDto(): List<BudgetDto> = map { it.toDto() }

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
