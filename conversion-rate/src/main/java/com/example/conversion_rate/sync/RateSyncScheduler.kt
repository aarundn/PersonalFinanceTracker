package com.example.conversion_rate.sync

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit

/**
 * Thin scheduler wrapping [WorkManager] for exchange-rate sync.
 *
 * Owns scheduling policy — the [RateSyncWorker] owns execution logic.
 */
class RateSyncScheduler(private val workManager: WorkManager) : RateSyncManager {

    /**
     * Enqueues a periodic job that refreshes exchange rates every [intervalHours].
     *
     * Uses [ExistingPeriodicWorkPolicy.KEEP] so subsequent calls are no-ops if already scheduled.
     */
    override fun schedulePeriodicSync(
        baseCurrency: String,
        providerId: String,
    ) {
        val request = PeriodicWorkRequestBuilder<RateSyncWorker>(
            DEFAULT_INTERVAL_HOURS, TimeUnit.HOURS
        )
            .setConstraints(networkConstraint())
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                30, TimeUnit.SECONDS,
            )

            .setInputData(inputData(baseCurrency, providerId))
            .build()

        workManager.enqueueUniquePeriodicWork(
            PERIODIC_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request,
        )
    }

    /**
     * Triggers a one-time immediate sync. Replaces any in-flight immediate work.
     */
    override fun triggerImmediateSync(baseCurrency: String, providerId: String) {
        val request = OneTimeWorkRequestBuilder<RateSyncWorker>()
            .setConstraints(networkConstraint())
            .setInputData(inputData(baseCurrency, providerId))
            .build()

        workManager.enqueueUniqueWork(
            IMMEDIATE_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request,
        )
    }

    override fun cancelPeriodicSync() {
        workManager.cancelUniqueWork(PERIODIC_WORK_NAME)
    }

    override fun cancelAll() {
        workManager.cancelUniqueWork(PERIODIC_WORK_NAME)
        workManager.cancelUniqueWork(IMMEDIATE_WORK_NAME)
    }

    /**
     * Observes the status of the most recent sync work (periodic or immediate).
     */
    override fun observeStatus(): Flow<SyncStatus> {
        return workManager
            .getWorkInfosForUniqueWorkFlow(PERIODIC_WORK_NAME)
            .map { infos ->
                val info = infos.firstOrNull()
                when (info?.state) {
                    WorkInfo.State.RUNNING -> SyncStatus.Syncing
                    WorkInfo.State.SUCCEEDED -> SyncStatus.Success(
                        System.currentTimeMillis()
                    )
                    WorkInfo.State.FAILED -> SyncStatus.Failed(
                        info.outputData.getString(RateSyncWorker.KEY_ERROR) ?: "Unknown error"
                    )
                    else -> SyncStatus.Idle
                }
            }
    }

    private fun networkConstraint() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(true)
        .build()

    private fun inputData(baseCurrency: String, providerId: String) = Data.Builder()
        .putString(RateSyncWorker.KEY_BASE_CURRENCY, baseCurrency)
        .putString(RateSyncWorker.KEY_PROVIDER_ID, providerId)
        .build()

    companion object {
        const val PERIODIC_WORK_NAME = "rate_sync_periodic"
        const val IMMEDIATE_WORK_NAME = "rate_sync_immediate"
        const val DEFAULT_INTERVAL_HOURS = 4L
    }
}
