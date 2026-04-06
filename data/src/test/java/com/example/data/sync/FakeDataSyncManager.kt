package com.example.data.sync

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeDataSyncManager : DataSyncManager {
    override fun schedulePeriodicSync() {

    }

    override fun triggerImmediateSync() {

    }

    override fun cancelAll() {

    }

    override fun observeSyncStatus(): Flow<DataSyncStatus> {
        return flowOf(DataSyncStatus.Idle)
    }
}