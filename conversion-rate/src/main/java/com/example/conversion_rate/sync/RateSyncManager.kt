package com.example.conversion_rate.sync

import kotlinx.coroutines.flow.Flow

/**
 * Contract for scheduling and managing exchange-rate background sync.
 */
interface RateSyncManager {
    fun schedulePeriodicSync(baseCurrency: String, providerId: String)
    fun triggerImmediateSync(baseCurrency: String, providerId: String)
    fun cancelPeriodicSync()
    fun cancelAll()
    fun observeStatus(): Flow<SyncStatus>
}
