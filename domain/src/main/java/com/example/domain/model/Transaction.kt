package com.example.domain.model


data class Transaction(
    val id: String,
    val userId: String,
    val amount: Double,
    val currency: String,
    val category: String,
    val date: Long,
    val notes: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val type: Type,
    )