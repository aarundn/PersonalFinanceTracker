package com.example.data.remote.exchangerate

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

/**
 * Ktor-based service for the Open ExchangeRate API.
 *
 * Free tier — no API key required, supports 150+ currencies including DZD.
 *
 * @see [Docs](https://www.exchangerate-api.com/docs/free)
 */
class ExchangeRateApiService(private val client: HttpClient) {

    /**
     * Fetches the latest rates for the given [baseCurrency].
     *
     * Returns all rates relative to the base — the caller picks the target.
     */
    suspend fun getLatestRates(baseCurrency: String): ExchangeRateApiResponse {
        return client.get("$BASE_URL/latest/$baseCurrency").body()
    }

    companion object {
        private const val BASE_URL = "https://open.er-api.com/v6"
    }
}
