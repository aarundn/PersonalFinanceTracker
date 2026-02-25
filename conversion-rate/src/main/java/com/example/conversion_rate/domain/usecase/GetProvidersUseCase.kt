package com.example.conversion_rate.domain.usecase

import com.example.conversion_rate.domain.repository.ExchangeRateRepository

/**
 * Use case for retrieving the list of available exchange-rate providers.
 *
 * Returns a list of (id, displayName) pairs.
 */
class GetProvidersUseCase(
    private val repository: ExchangeRateRepository,
) {
    suspend operator fun invoke(): Result<List<Pair<String, String>>> =
        repository.getProviders()
}
