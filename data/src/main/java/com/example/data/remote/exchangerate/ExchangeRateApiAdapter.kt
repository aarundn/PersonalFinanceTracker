package com.example.data.remote.exchangerate

import com.example.conversion_rate.domain.port.ExchangeRateProviderPort
import java.math.BigDecimal

/**
 * Adapter implementing [ExchangeRateProviderPort] using the Open ExchangeRate API.
 *
 * Supports 150+ currencies.
 * Free tier — no API key required.
 */
class ExchangeRateApiAdapter(
    private val apiService: ExchangeRateApiService,
) : ExchangeRateProviderPort {

    override val id: String = PROVIDER_ID
    override val displayName: String = PROVIDER_DISPLAY_NAME

    override suspend fun getRate(
        fromCurrencyCode: String,
        toCurrencyCode: String,
    ): Result<BigDecimal> = runCatching {
        val response = apiService.getLatestRates(baseCurrency = fromCurrencyCode)

        if (response.result != "success") {
            throw IllegalStateException(
                "ExchangeRate-API returned result: ${response.result}"
            )
        }

        val rate = response.rates[toCurrencyCode]
            ?: throw NoSuchElementException(
                "Rate not found for $toCurrencyCode in ExchangeRate-API response"
            )

        BigDecimal.valueOf(rate)
    }

    companion object {
        const val PROVIDER_ID = "exchangerate_api"
        const val PROVIDER_DISPLAY_NAME = "ExchangeRate-API"
    }
}
