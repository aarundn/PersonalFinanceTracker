package com.example.conversion_rate.data.local

import androidx.room.Entity

@Entity(
    tableName = "conversion_rates",
    primaryKeys = ["providerId", "fromCurrency", "toCurrency"]
)
data class ConversionRateEntity(
    val providerId: String,
    val fromCurrency: String,
    val toCurrency: String,
    val rate: Double,
    val lastUpdatedMillis: Long
)
