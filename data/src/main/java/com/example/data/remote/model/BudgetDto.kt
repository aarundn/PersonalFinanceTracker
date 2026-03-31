package com.example.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BudgetDto(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    val category: String,
    val amount: Double,
    val currency: String,
    val period: String,
    val notes: String? = null,
    @SerialName("created_at")
    val createdAt: Long,
    @SerialName("updated_at")
    val updatedAt: Long
)
