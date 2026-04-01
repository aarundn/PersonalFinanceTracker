package com.example.data.sync

import kotlinx.coroutines.flow.Flow

/**
 * Contract for scheduling and managing data synchronization for Transactions and Budgets.
 *
 * A single [DataSyncWorker] handles both entity types in one run, so [observeSyncStatus]
 * reflects the state of the entire sync operation.
 */
interface DataSyncManager {
    fun schedulePeriodicSync()
    fun triggerImmediateSync()
    fun cancelAll()
    fun observeSyncStatus(): Flow<DataSyncStatus>
}
