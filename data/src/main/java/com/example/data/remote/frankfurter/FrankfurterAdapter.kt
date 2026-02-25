package com.example.data.remote.frankfurter

import com.example.conversion_rate.domain.port.ExchangeRateProviderPort
import java.math.BigDecimal

/**
 * Adapter implementing [ExchangeRateProviderPort] using the Frankfurter API.
 *
 * Delegates HTTP communication to [FrankfurterApiService] and maps the
 * response to the port's contract.
 */
class FrankfurterAdapter(
    private val apiService: FrankfurterApiService,
) : ExchangeRateProviderPort {

    override val id: String = PROVIDER_ID
    override val displayName: String = PROVIDER_DISPLAY_NAME

    override suspend fun getRate(
        fromCurrencyCode: String,
        toCurrencyCode: String,
    ): Result<BigDecimal> = runCatching {
        val response = apiService.getLatestRate(
            from = fromCurrencyCode,
            to = toCurrencyCode,
        )
        val rate = response.rates[toCurrencyCode]
            ?: throw NoSuchElementException(
                "Rate not found for $toCurrencyCode in Frankfurter response"
            )
        BigDecimal.valueOf(rate)
    }

    companion object {
        const val PROVIDER_ID = "frankfurter"
        const val PROVIDER_DISPLAY_NAME = "Frankfurter"
    }
}
