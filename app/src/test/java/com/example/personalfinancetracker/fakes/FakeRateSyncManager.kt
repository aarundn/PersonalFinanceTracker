package com.example.personalfinancetracker.fakes

import com.example.conversion_rate.sync.RateSyncManager
import com.example.conversion_rate.sync.SyncStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Fake implementation of [RateSyncManager] for unit testing.
 * Records all calls and exposes them for assertion.
 */
class FakeRateSyncManager : RateSyncManager {

    data class SyncCall(val baseCurrency: String, val providerId: String)

    private val _immediateSyncCalls = mutableListOf<SyncCall>()
    val immediateSyncCalls: List<SyncCall> get() = _immediateSyncCalls

    private val _periodicSyncCalls = mutableListOf<SyncCall>()
    val periodicSyncCalls: List<SyncCall> get() = _periodicSyncCalls

    var cancelPeriodicSyncCalled = false
        private set

    var cancelAllCalled = false
        private set

    private val _syncStatus = MutableStateFlow<SyncStatus>(SyncStatus.Idle)

    fun setSyncStatus(status: SyncStatus) {
        _syncStatus.value = status
    }

    override fun schedulePeriodicSync(baseCurrency: String, providerId: String) {
        _periodicSyncCalls.add(SyncCall(baseCurrency, providerId))
    }

    override fun triggerImmediateSync(baseCurrency: String, providerId: String) {
        _immediateSyncCalls.add(SyncCall(baseCurrency, providerId))
    }

    override fun cancelPeriodicSync() {
        cancelPeriodicSyncCalled = true
    }

    override fun cancelAll() {
        cancelAllCalled = true
    }

    override fun observeStatus(): Flow<SyncStatus> = _syncStatus
}
