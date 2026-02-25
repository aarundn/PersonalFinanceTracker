package com.example.data.remote.exchangerate

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response model from the Open ExchangeRate API.
 *
 * Example response:
 * ```json
 * {
 *   "result": "success",
 *   "base_code": "USD",
 *   "rates": { "DZD": 134.50, "EUR": 0.92 }
 * }
 * ```
 *
 * @see [Docs](https://www.exchangerate-api.com/docs/free)
 */
@Serializable
data class ExchangeRateApiResponse(
    val result: String,
    @SerialName("base_code") val baseCode: String,
    val rates: Map<String, Double>,
)
