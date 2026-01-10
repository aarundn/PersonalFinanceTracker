package com.example.data.remote.model

import com.google.firebase.database.PropertyName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class TransactionsDto(
    val id: String? = null,
    @PropertyName("user_id")
    val userId: String,
    val amount: Double? = null,
    val description: String? = null,
    @PropertyName("icon_url")
    val iconUrl : String? = null,
    val date: Date? = null,
    val category: String? = null,
    val type: String? = null,
)