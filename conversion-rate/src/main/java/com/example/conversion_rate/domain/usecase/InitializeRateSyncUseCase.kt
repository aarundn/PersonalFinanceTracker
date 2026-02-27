package com.example.conversion_rate.domain.usecase

import com.example.conversion_rate.sync.RateSyncManager

/**
 * Initializes the background synchronization of exchange rates.
 * Ensures that both periodic and immediate syncing target the active [baseCurrency]
 * and [providerId].
 */
class InitializeRateSyncUseCase(
    private val syncManager: RateSyncManager,
) {
    /**
     * Schedules periodic syncing and triggers an immediate sync for the selected pair.
     */
    operator fun invoke(baseCurrency: String, providerId: String) {
        syncManager.triggerImmediateSync(baseCurrency, providerId)
        syncManager.schedulePeriodicSync(baseCurrency, providerId)
    }
}
