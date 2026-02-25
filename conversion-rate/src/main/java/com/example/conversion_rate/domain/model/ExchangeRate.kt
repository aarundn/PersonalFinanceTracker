package com.example.conversion_rate.domain.model

import java.math.BigDecimal

/**
 * Pure domain model representing a cached exchange rate.
 */
data class ExchangeRate(
    val fromCurrency: String,
    val toCurrency: String,
    val rate: BigDecimal,
    val timestampMillis: Long,
)
