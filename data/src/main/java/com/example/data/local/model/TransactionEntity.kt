package com.example.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.domain.model.Type

@Entity(
    tableName = "transactions",
    indices = [
        Index(value = ["budgetId"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = BudgetEntity::class,
            parentColumns = ["id"],
            childColumns = ["budgetId"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class TransactionEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val type: Type,
    val amount: Double,
    val currency: String,
    val category: String,
    val date: Long,
    val notes: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val budgetId: String? = null,
    val syncStatus: String = "PENDING"
)