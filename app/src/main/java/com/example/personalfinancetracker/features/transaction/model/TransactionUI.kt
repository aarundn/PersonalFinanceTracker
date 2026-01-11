package com.example.personalfinancetracker.features.transaction.model

import com.example.domain.model.Type
import java.util.Date

data class TransactionUI(
    val id: String,
    val userId: String,
    val amount: Double,
    val description: String,
    val iconUrl: String,
    val date: Date,
    val category: String,
    val type: Type,
)