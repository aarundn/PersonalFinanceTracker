package com.example.conversion_rate.domain.repository

import java.math.BigDecimal

/**
 * Driving port (contract) for the conversion-rate module.
 *
 * The outside world interacts with this module exclusively through this interface.
 * Implementations handle offline-first caching and remote fetching via driven ports.
 */
interface ExchangeRateRepository {

    /**
     * Converts [amount] from [fromCurrencyCode] to [toCurrencyCode].
     *
     * Offline-first: uses cached rate if fresh, fetches from the provider
     * otherwise, and falls back to stale cache when the network is unavailable.
     */
    suspend fun convert(
        providerId: String,
        fromCurrencyCode: String,
        toCurrencyCode: String,
        amount: BigDecimal,
    ): Result<BigDecimal>

    /**
     * Returns the list of available exchange-rate providers as (id, displayName) pairs.
     */
    suspend fun getProviders(): Result<List<Pair<String, String>>>
}
