package com.example.personalfinancetracker.features.transaction.model

import androidx.compose.runtime.Immutable
import com.example.core.utils.parseDateString
import com.example.domain.model.Type

@Immutable
data class TransactionUi(
    val id: String,
    val userId: String,
    val amount: Double,
    val currency: String,
    val category: String,
    val date: Long,
    val notes: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val type: Type,
) {
    val formattedDate: String get() = parseDateString(date)
    val isIncome: Boolean get() = type == Type.INCOME
}