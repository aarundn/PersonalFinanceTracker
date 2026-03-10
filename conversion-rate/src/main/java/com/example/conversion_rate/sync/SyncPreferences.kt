package com.example.conversion_rate.sync

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.conversionDataStore: DataStore<Preferences> by preferencesDataStore(name = "sync_preferences")

class SyncPreferences(private val context: Context) {

    private companion object {
        val LAST_SYNC_TIME_KEY = longPreferencesKey("last_sync_time")
    }

    val lastSyncTime: Flow<Long?> = context.conversionDataStore.data.map { prefs ->
        prefs[LAST_SYNC_TIME_KEY]
    }

    suspend fun saveLastSyncTime(timeInMillis: Long) {
        context.conversionDataStore.edit { prefs ->
            prefs[LAST_SYNC_TIME_KEY] = timeInMillis
        }
    }
}
