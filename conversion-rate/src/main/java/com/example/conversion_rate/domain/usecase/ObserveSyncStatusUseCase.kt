package com.example.conversion_rate.domain.usecase

import com.example.conversion_rate.domain.repository.ExchangeRateRepository
import kotlinx.coroutines.flow.Flow

/**
 * UseCase for observing the real-time background synchronization status.
 */
class ObserveSyncStatusUseCase(
    private val repository: ExchangeRateRepository
) {
    operator fun invoke(): Flow<String> = repository.observeSyncStatus()
}
