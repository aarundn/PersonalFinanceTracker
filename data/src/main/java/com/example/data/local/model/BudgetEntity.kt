package com.example.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val category: String,
    val amount: Double,
    val currency: String,
    val period: String,
    val notes: String?,
    val createdAt: Long,
    val updatedAt: Long,
)
