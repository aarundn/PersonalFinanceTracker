package com.example.data.remote.frankfurter

import kotlinx.serialization.Serializable

/**
 * Response model from the Frankfurter API.
 *
 * Example response:
 * ```json
 * {
 *   "amount": 1.0,
 *   "base": "USD",
 *   "date": "2025-02-24",
 *   "rates": { "EUR": 0.9150 }
 * }
 * ```
 */
@Serializable
data class FrankfurterResponse(
    val amount: Double,
    val base: String,
    val date: String,
    val rates: Map<String, Double>,
)
