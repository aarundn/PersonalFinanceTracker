package com.example.data.sync

/**
 * Represents the current state of the background data sync job (Transactions + Budgets).
 *
 * This is the UI-facing contract, derived from WorkManager's [WorkInfo.State].
 * Per-record status is tracked separately via [SyncStatusEnum] on each entity.
 */
sealed interface DataSyncStatus {
    data object Idle : DataSyncStatus
    data object Syncing : DataSyncStatus
    data object Success : DataSyncStatus
    data class Failed(val error: String) : DataSyncStatus
}
