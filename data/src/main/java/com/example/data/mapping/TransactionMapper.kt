package com.example.data.mapping

import com.example.data.local.model.TransactionEntity
import com.example.data.remote.model.TransactionsDto
import com.example.domain.model.Transaction
import com.example.domain.model.Type

/*fun Transaction.toDto(): TransactionsDto {
//    return TransactionsDto(
//        id = id,
//        userId = userId,
//        amount = amount,
//        description = description,
//        iconUrl = iconUrl,
//        date = date,
//        category = category,
//        type = type.name
//    )
//}
//
//fun TransactionsDto.toDomain(): Transaction {
//    return Transaction(
//        id = id,
//        userId = userId ,
//        amount = amount,
//        description = description ?: "",
//        iconUrl = iconUrl ?: "",
//        date = date,
//        category = category,
//        type = try {
//            Type.valueOf(type)
//        } catch (e: IllegalArgumentException) {
//            Type.EXPENSE
//        }
//    )
//}
//
//fun List<TransactionsDto>.toDomain(): List<Transaction> = map { it.toDomain() }
/fun List<Transaction>.toDto(): List<TransactionsDto> = map { it.toDto() }*/

fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = id,
        userId = userId,
        amount = amount,
        currency = currency,
        categoryId = categoryId,
        date = date,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt,
        type = type
    )
}
fun Transaction.toEntity(): TransactionEntity{
    return TransactionEntity(
        id = id,
        userId = userId,
        amount = amount,
        currency = currency,
        categoryId = categoryId,
        date = date,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt,
        type = type
    )
}
fun List<TransactionEntity>.toDomain(): List<Transaction> = map { it.toDomain() }
fun List<Transaction>.toEntity(): List<TransactionEntity> = map { it.toEntity() }


