package com.example.conversion_rate.domain.usecase

import com.example.conversion_rate.domain.repository.ExchangeRateRepository
import java.math.BigDecimal

/**
 * Use case for converting an amount from one currency to another.
 *
 * Depends on the [ExchangeRateRepository] contract, which handles
 * offline-first caching and provider communication internally.
 */
class ConvertCurrencyUseCase(
    private val repository: ExchangeRateRepository,
) {
    suspend operator fun invoke(
        providerId: String,
        fromCurrencyCode: String,
        toCurrencyCode: String,
        amount: BigDecimal,
    ): Result<BigDecimal> = repository.convert(providerId, fromCurrencyCode, toCurrencyCode, amount)
}
