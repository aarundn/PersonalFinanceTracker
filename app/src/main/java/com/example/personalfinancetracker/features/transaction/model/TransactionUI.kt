package com.example.personalfinancetracker.features.transaction.model

import androidx.compose.runtime.Immutable
import com.example.core.model.Category
import com.example.core.model.DefaultCategories
import com.example.core.model.DefaultCurrencies
import com.example.core.utils.parseDateString
import com.example.domain.model.Type

@Immutable
data class TransactionUi(
    val id: String,
    val userId: String,
    val amount: Double,
    val currency: String,
    val currencySymbol: String = DefaultCurrencies.fromId(currency)?.symbol ?: currency,
    val categoryId: String,
    val date: Long,
    val notes: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val currentCategory: Category = DefaultCategories.fromId(categoryId) ?: DefaultCategories.OTHER,
    val type: Type,
) {
    val formattedDate: String get() = parseDateString(date)
    val formattedTime: String get() = parseDateString(date, isDate = false)
    val isIncome: Boolean get() = type == Type.INCOME
}