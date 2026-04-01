package com.example.data.sync

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.concurrent.TimeUnit

/**
 * Thin scheduler wrapping [WorkManager] for data sync.
 *
 * Owns scheduling policy — [DataSyncWorker] owns the execution logic.
 * A single [DataSyncWorker] handles both Transactions and Budgets per run.
 */
class DataSyncScheduler(
    private val workManager: WorkManager,
) : DataSyncManager {

    /**
     * Enqueues a periodic job every [DEFAULT_INTERVAL_MINUTES].
     * Uses [ExistingPeriodicWorkPolicy.KEEP] so subsequent calls are no-ops if already scheduled.
     */
    override fun schedulePeriodicSync() {
        val request = PeriodicWorkRequestBuilder<DataSyncWorker>(
            DEFAULT_INTERVAL_MINUTES, TimeUnit.MINUTES
        )
            .setConstraints(networkConstraint())
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            PERIODIC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    /**
     * Triggers a one-time immediate sync. Replaces any in-flight immediate work.
     */
    override fun triggerImmediateSync() {
        val request = OneTimeWorkRequestBuilder<DataSyncWorker>()
            .setConstraints(networkConstraint())
            .build()

        workManager.enqueueUniqueWork(
            IMMEDIATE_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    override fun cancelAll() {
        workManager.cancelUniqueWork(PERIODIC_WORK_NAME)
        workManager.cancelUniqueWork(IMMEDIATE_WORK_NAME)
    }

    /**
     * Combines periodic and immediate WorkInfo into a single [DataSyncStatus].
     * Immediate work takes precedence if currently active.
     */
    override fun observeSyncStatus(): Flow<DataSyncStatus> {
        return combine(
            workManager.getWorkInfosForUniqueWorkFlow(PERIODIC_WORK_NAME),
            workManager.getWorkInfosForUniqueWorkFlow(IMMEDIATE_WORK_NAME),
        ) { periodicInfos, immediateInfos ->
            // Prefer immediate work status if it is actively running/enqueued
            val info = immediateInfos.firstOrNull()?.takeIf {
                it.state == WorkInfo.State.RUNNING || it.state == WorkInfo.State.ENQUEUED
            } ?: periodicInfos.firstOrNull()

            when (info?.state) {
                WorkInfo.State.RUNNING  -> DataSyncStatus.Syncing
                WorkInfo.State.SUCCEEDED -> DataSyncStatus.Success
                WorkInfo.State.FAILED   -> DataSyncStatus.Failed(
                    info.outputData.getString(DataSyncWorker.KEY_ERROR) ?: "Sync failed"
                )
                WorkInfo.State.ENQUEUED -> DataSyncStatus.Idle
                else                    -> DataSyncStatus.Idle
            }
        }
    }


    private fun networkConstraint() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    companion object {
        const val PERIODIC_WORK_NAME = "data_sync_periodic"
        const val IMMEDIATE_WORK_NAME = "data_sync_immediate"
        const val DEFAULT_INTERVAL_MINUTES = 15L
    }
}
