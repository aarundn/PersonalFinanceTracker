package com.example.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.Type

@Entity(
    tableName = "transactions",
//    indices = [
//        Index(value = ["categoryId"])
//    ],
//    foreignKeys = [
//        androidx.room.ForeignKey(
//            entity = CategoryEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["categoryId"],
//            onDelete = androidx.room.ForeignKey.RESTRICT
//        )
//    ]
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
    val updatedAt: Long
)