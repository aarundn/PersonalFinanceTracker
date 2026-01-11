package com.example.domain.model


data class Transaction(
    val id: String,
    val userId: String,
    val amount: Double,
    val description: String,
    val iconUrl: String,
    val date: Long,
    val category: String,
    val type: Type,
    )