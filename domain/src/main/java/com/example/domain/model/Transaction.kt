package com.example.domain.model

import java.util.Date


data class Transaction(
    val id: String,
    val userId: String,
    val amount: Double,
    val description: String,
    val iconUrl: String,
    val date: Date,
    val category: String,
    val type: Type,
    )