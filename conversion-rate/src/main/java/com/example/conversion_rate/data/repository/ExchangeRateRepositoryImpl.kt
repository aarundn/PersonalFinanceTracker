package com.example.conversion_rate.data.repository

import android.util.Log
import com.example.conversion_rate.data.local.ConversionRateDao
import com.example.conversion_rate.data.local.ConversionRateEntity
import com.example.conversion_rate.data.repository.ExchangeRateRepositoryImpl.Companion.CACHE_TTL_MILLIS
import com.example.conversion_rate.domain.port.ExchangeRateProviderPort
import com.example.conversion_rate.domain.repository.ExchangeRateRepository
import com.example.conversion_rate.sync.RateSyncManager
import java.io.IOException
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
    private val syncManager: RateSyncManager
) : ExchangeRateRepository {

    override suspend fun convert(
        providerId: String,
        fromCurrencyCode: String,
        toCurrencyCode: String,
        amount: BigDecimal,
    ): Result<BigDecimal> = runCatching {
        val rate = getOrFetchRate(providerId, fromCurrencyCode, toCurrencyCode)
        amount.multiply(rate)
    }

    override suspend fun getProviders(): Result<List<Pair<String, String>>> =
        Result.success(providers.map { it.id to it.displayName })

    override suspend fun syncRate(
        providerId: String,
        fromCurrencyCode: String,
        toCurrencyCode: String,
    ): Result<Unit> = runCatching {
        val provider = findProvider(providerId)
        val freshRate = provider.getRate(fromCurrencyCode, toCurrencyCode).getOrThrow()
        cacheRate(providerId, fromCurrencyCode, toCurrencyCode, freshRate)
    }


    private suspend fun getOrFetchRate(
        providerId: String,
        from: String,
        to: String,
    ): BigDecimal {
        val cached = dao.getRate(providerId, from, to)

        if (cached != null) return cached.rateBigDecimal

        return fetchAndCache(providerId, from, to)
            .onSuccess {
                syncManager.triggerImmediateSync(from, providerId)
            }
            .getOrElse { error ->
                Log.e("ExchangeRateRepository", "Failed to fetch rate", error)
                if (error is IOException) {
                    throw Exception("Network error: Please connect to the internet to initialize exchange rates for the first time.")
                }
                throw Exception(
                    "Fetch failed. The provider might not support converting $from to $to." +
                            " Please change the provider in Settings and try again.",
                )
            }
    }

    private suspend fun fetchAndCache(
        providerId: String,
        from: String,
        to: String,
    ): Result<BigDecimal> = runCatching {
        val provider = findProvider(providerId)
        val rate = provider.getRate(from, to).getOrThrow()
        cacheRate(providerId, from, to, rate)
        rate
    }

    private fun findProvider(id: String): ExchangeRateProviderPort =
        providers.find { it.id == id }
            ?: throw IllegalArgumentException("Unknown provider: $id")


    private suspend fun cacheRate(
        providerId: String,
        from: String,
        to: String,
        rate: BigDecimal,
    ) {
        dao.insertRate(
            ConversionRateEntity(
                providerId = providerId,
                fromCurrency = from,
                toCurrency = to,
                rate = rate.toDouble(),
                lastUpdatedMillis = System.currentTimeMillis(),
            )
        )
    }

    private val ConversionRateEntity.rateBigDecimal: BigDecimal
        get() = BigDecimal.valueOf(rate)

    companion object {
        /** Cache time-to-live: 1 hour. */
        private const val CACHE_TTL_MILLIS = 60 * 60 * 1000L
    }
}
