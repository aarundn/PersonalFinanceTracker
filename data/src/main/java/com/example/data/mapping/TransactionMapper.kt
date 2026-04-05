package com.example.data.mapping

import com.example.data.local.model.TransactionEntity
import com.example.data.remote.model.TransactionsDto
import com.example.data.sync.SyncStatusEnum
import com.example.domain.model.Transaction
import com.example.domain.model.Type

fun TransactionEntity.toDto(): TransactionsDto {
    return TransactionsDto(
        id = id,
        userId = userId,
        currency = currency,
        amount = amount,
        description = notes,
        date = date,
        category = category,
        type = type.name,
        createdAt = createdAt,
        updatedAt = updatedAt,
        budgetId = budgetId
    )
}

fun TransactionsDto.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        userId = userId,
        amount = amount,
        currency = currency,
        category = category,
        date = date,
        notes = description ?: "",
        createdAt = createdAt,
        updatedAt = updatedAt,
        type = Type.valueOf(type),
        budgetId = budgetId,
        syncStatus = SyncStatusEnum.SYNCED.name
    )
}


fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = id,
        userId = userId,
        amount = amount,
        currency = currency,
        category = category,
        date = date,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt,
        type = type,
        budgetId = budgetId,
        syncStatus = syncStatus
    )
}
fun Transaction.toEntity(): TransactionEntity{
    return TransactionEntity(
        id = id,
        userId = userId,
        amount = amount,
        currency = currency,
        category = category,
        date = date,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt,
        type = type,
        budgetId = budgetId,
        syncStatus = syncStatus
    )
}
fun List<TransactionEntity>.toDomain(): List<Transaction> = map { it.toDomain() }
fun List<Transaction>.toEntity(): List<TransactionEntity> = map { it.toEntity() }


