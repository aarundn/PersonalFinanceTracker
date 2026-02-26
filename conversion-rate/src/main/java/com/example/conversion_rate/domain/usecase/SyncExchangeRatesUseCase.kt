package com.example.conversion_rate.domain.usecase

import com.example.conversion_rate.domain.repository.ExchangeRateRepository

/**
 * Orchestrates syncing exchange rates for all registered providers.
 *
 * Called by [RateSyncWorker] during background sync.
 */
class SyncExchangeRatesUseCase(
    private val repository: ExchangeRateRepository,
) {
    /**
     * Syncs rates from [providerId] for [baseCurrency] against [DEFAULT_TARGETS].
     */
    suspend operator fun invoke(baseCurrency: String, providerId: String): Result<Unit> =
        runCatching {
            DEFAULT_TARGETS
                .filter { it != baseCurrency }
                .forEach { target ->
                    repository.syncRate(
                        providerId,
                        baseCurrency,
                        target
                    ).getOrThrow()
                }
        }

    companion object {
        val DEFAULT_TARGETS = listOf(
            "DZD", "USD", "EUR", "GBP", "JPY",
            "CAD", "AUD", "CHF", "CNY", "INR", "BRL",
        )
    }
}
