package com.example.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionsDto(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    val amount: Double,
    val description: String? = null,
    @SerialName("icon_url")
    val iconUrl: String? = null,
    val date: Long,
    val category: String,
    val type: String,
)