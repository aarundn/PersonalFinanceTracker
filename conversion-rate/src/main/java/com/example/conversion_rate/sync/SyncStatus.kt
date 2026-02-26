package com.example.conversion_rate.sync

/**
 * Represents the current state of exchange-rate background sync.
 */
sealed interface SyncStatus {
    data object Idle : SyncStatus
    data object Syncing : SyncStatus
    data class Success(val timestamp: Long) : SyncStatus
    data class Failed(val error: String) : SyncStatus
}
