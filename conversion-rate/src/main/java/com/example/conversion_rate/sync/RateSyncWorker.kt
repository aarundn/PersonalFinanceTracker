package com.example.conversion_rate.sync

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.example.conversion_rate.domain.usecase.SyncExchangeRatesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * [CoroutineWorker] that refreshes cached exchange rates in the background.
 *
 * Thin shell — delegates all business logic to [SyncExchangeRatesUseCase].
 */
class RateSyncWorker(
    context: Context,
    params: WorkerParameters,
    private val syncExchangeRates: SyncExchangeRatesUseCase,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val baseCurrency = inputData.getString(KEY_BASE_CURRENCY)
            ?: return@withContext failure("Missing base currency")

        val providerId = inputData.getString(KEY_PROVIDER_ID)
            ?: return@withContext failure("Missing provider ID")

        Log.d(TAG, "Starting rate sync for base=$baseCurrency, provider=$providerId")
        try {
            syncExchangeRates(baseCurrency, providerId).onSuccess {
                Log.d(TAG, "Rate sync completed")
                Result.success()
            }.onFailure {
                Log.e(TAG, "Rate sync failed", it)
                if (runAttemptCount < MAX_RETRIES) Result.retry()
                else failure(it.message)
            }
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing rates", e)
            failure(e.message)
        }
    }

    private fun failure(message: String?) = Result.failure(
        Data.Builder().putString(KEY_ERROR, message ?: "Unknown error").build()
    )

    companion object {
        const val TAG = "RateSyncWorker"
        const val KEY_BASE_CURRENCY = "base_currency"
        const val KEY_PROVIDER_ID = "provider_id"
        const val KEY_ERROR = "error"
        private const val MAX_RETRIES = 3
    }
}
