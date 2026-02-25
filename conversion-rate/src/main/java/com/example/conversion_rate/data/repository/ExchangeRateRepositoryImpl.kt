package com.example.conversion_rate.data.repository

import com.example.conversion_rate.data.local.ConversionRateDao
import com.example.conversion_rate.data.local.ConversionRateEntity
import com.example.conversion_rate.domain.port.ExchangeRateProviderPort
import com.example.conversion_rate.domain.repository.ExchangeRateRepository
import java.math.BigDecimal

/**
 * Offline-first implementation of [ExchangeRateRepository].
 *
 * Supports multiple [ExchangeRateProviderPort] adapters injected from outside the module.
 *
 * Strategy:
 * 1. Look up the requested provider by [providerId].
 * 2. Check local Room cache for a fresh rate (< [CACHE_TTL_MILLIS]).
 * 3. If fresh → multiply and return.
 * 4. If stale or missing → fetch from the provider, save to cache, return.
 * 5. If network fails and stale cache exists → fallback to stale rate.
 * 6. If network fails and no cache → propagate the error.
 */
class ExchangeRateRepositoryImpl(
    private val providers: List<ExchangeRateProviderPort>,
    private val dao: ConversionRateDao,
) : ExchangeRateRepository {

    override suspend fun convert(
        providerId: String,
        fromCurrencyCode: String,
        toCurrencyCode: String,
        amount: BigDecimal,
    ): Result<BigDecimal> {

        val provider = providers.find { it.id == providerId }
            ?: return Result.failure(
                IllegalArgumentException("Unknown provider: $providerId")
            )

        val cached = dao.getRate(
            providerId = providerId,
            from = fromCurrencyCode,
            to = toCurrencyCode,
        )

        val now = System.currentTimeMillis()

        if (cached != null && (now - cached.lastUpdatedMillis) < CACHE_TTL_MILLIS) {
            val rate = BigDecimal.valueOf(cached.rate)
            return Result.success(amount.multiply(rate))
        }

        val fetchResult = provider.getRate(fromCurrencyCode, toCurrencyCode)

        return fetchResult.fold(
            onSuccess = { freshRate ->
                dao.insertRate(
                    ConversionRateEntity(
                        providerId = providerId,
                        fromCurrency = fromCurrencyCode,
                        toCurrency = toCurrencyCode,
                        rate = freshRate.toDouble(),
                        lastUpdatedMillis = now,
                    )
                )
                Result.success(amount.multiply(freshRate))
            },
            onFailure = { error ->
                if (cached != null) {
                    val staleRate = BigDecimal.valueOf(cached.rate)
                    Result.success(amount.multiply(staleRate))
                } else {
                    Result.failure(error)
                }
            }
        )
    }

    override suspend fun getProviders(): Result<List<Pair<String, String>>> {
        return Result.success(providers.map { it.id to it.displayName })
    }

    companion object {
        /** Cache time-to-live: 1 hour. */
        private const val CACHE_TTL_MILLIS = 60 * 60 * 1000L
    }
}
