package com.example.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionsDto(
    val id: String = "",
    @SerialName("user_id")
    val userId: String = "",
    val currency: String = "",
    val amount: Double = 0.0,
    val description: String? = null,
    val date: Long = 0L,
    val category: String = "",
    val type: String = "",
    @SerialName("created_at")
    val createdAt: Long = 0L,
    @SerialName("updated_at")
    val updatedAt: Long = 0L,
    @SerialName("budget_id")
    val budgetId: String? = null
)