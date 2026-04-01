package com.example.data.sync

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.example.domain.repo.BudgetRepository
import com.example.domain.repo.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * A single [CoroutineWorker] that syncs both Transactions and Budgets in one run.
 *
 * Thin shell — delegates sync logic to [TransactionRepository] and [BudgetRepository].
 * Both sync operations run sequentially. If one fails, the worker retries the whole job.
 * Per-record status is tracked via [SyncStatusEnum] on each entity.
 */
class DataSyncWorker(
    context: Context,
    params: WorkerParameters,
    private val transactionRepository: TransactionRepository,
    private val budgetRepository: BudgetRepository,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        Log.d(TAG, "Starting data sync (transactions + budgets)")
        return@withContext try {
            transactionRepository.syncWithRemote().getOrThrow()
            Log.d(TAG, "Transaction sync complete")

            budgetRepository.syncWithRemote().getOrThrow()
            Log.d(TAG, "Budget sync complete")


            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Data sync failed", e)
            if (runAttemptCount < MAX_RETRIES) Result.retry()
            else failure(e.message)
        }
    }

    private fun failure(message: String?) = Result.failure(
        Data.Builder().putString(KEY_ERROR, message ?: "Unknown error").build()
    )


    companion object {
        const val TAG = "DataSyncWorker"
        const val KEY_ERROR = "error"
        private const val MAX_RETRIES = 3
    }
}
