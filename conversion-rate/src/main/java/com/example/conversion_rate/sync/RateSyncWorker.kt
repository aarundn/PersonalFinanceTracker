package com.example.conversion_rate.sync

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.example.conversion_rate.domain.usecase.SyncExchangeRatesUseCase
import com.example.conversion_rate.util.ConversionConstants
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
            ?: return@withContext failure(ConversionConstants.ERROR_MISSING_BASE_CURRENCY)

        val providerId = inputData.getString(KEY_PROVIDER_ID)
            ?: return@withContext failure(ConversionConstants.ERROR_MISSING_PROVIDER_ID)

        Log.d(TAG, "Starting rate sync for base=$baseCurrency, provider=$providerId")
        return@withContext try {
            Log.d(TAG, "Syncing rates...")
            syncExchangeRates(baseCurrency, providerId).getOrThrow()
            
            Log.d(TAG, "Rate sync completed")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Rate sync failed", e)
            if (runAttemptCount < MAX_RETRIES) Result.retry()
            else failure(e.message)
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
