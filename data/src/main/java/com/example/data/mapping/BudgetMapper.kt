package com.example.data.mapping

import com.example.data.local.model.BudgetEntity
import com.example.data.remote.model.BudgetDto
import com.example.domain.model.Budget
import com.example.data.sync.SyncStatusEnum

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
        syncStatus = SyncStatusEnum.SYNCED.name
    )
}


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
        syncStatus = syncStatus
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
        syncStatus = syncStatus
    )
}

fun List<BudgetEntity>.toDomain(): List<Budget> = map { it.toDomain() }
fun List<Budget>.toEntity(): List<BudgetEntity> = map { it.toEntity() }

