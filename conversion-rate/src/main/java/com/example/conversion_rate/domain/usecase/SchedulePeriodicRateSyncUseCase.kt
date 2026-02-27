package com.example.conversion_rate.domain.usecase

import com.example.conversion_rate.sync.RateSyncManager

/**
 * Schedules the periodic background synchronization of exchange rates on app startup.
 * Unlike [InitializeRateSyncUseCase], this does NOT trigger an immediate sync,
 * avoiding redundant API calls when the app is simply opened (since the repository
 * already fetches on-demand if data is missing).
 */
class SchedulePeriodicRateSyncUseCase(
    private val syncManager: RateSyncManager,
) {
    operator fun invoke(baseCurrency: String, providerId: String) {
        syncManager.schedulePeriodicSync(baseCurrency, providerId)
    }
}
