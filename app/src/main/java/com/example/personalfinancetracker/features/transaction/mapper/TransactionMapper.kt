package com.example.personalfinancetracker.features.transaction.mapper

import com.example.domain.model.Transaction
import com.example.personalfinancetracker.features.transaction.model.TransactionUi

fun Transaction.toTransactionUi(): TransactionUi {
    return TransactionUi(
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
    )
}

fun TransactionUi.toTransaction(): Transaction {
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
    )
}

fun List<Transaction>.toTransactionUi(): List<TransactionUi> = map { it.toTransactionUi() }

fun List<TransactionUi>.toTransaction(): List<Transaction> = map { it.toTransaction() }
