package com.example.domain.model

data class Budget(
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
