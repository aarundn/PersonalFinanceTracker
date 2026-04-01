package com.example.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BudgetDto(
    val id: String = "",
    @SerialName("user_id")
    val userId: String = "",
    val category: String = "",
    val amount: Double = 0.00,
    val currency: String = "",
    val period: String = "",
    val notes: String? = null,
    @SerialName("created_at")
    val createdAt: Long = 0L,
    @SerialName("updated_at")
    val updatedAt: Long = 0L
)
