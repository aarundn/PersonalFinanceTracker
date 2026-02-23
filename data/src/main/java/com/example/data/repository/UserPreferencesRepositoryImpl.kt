package com.example.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.domain.repo.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepositoryImpl(
    private val context: Context
) : UserPreferencesRepository {

    private companion object {
        val BASE_CURRENCY_KEY = stringPreferencesKey("base_currency")
        const val DEFAULT_CURRENCY = "DZD"
    }

    override val baseCurrency: Flow<String> =
        context.dataStore.data.map { prefs ->
            prefs[BASE_CURRENCY_KEY] ?: DEFAULT_CURRENCY
        }

    override suspend fun setBaseCurrency(currencyId: String) {
        context.dataStore.edit { prefs ->
            prefs[BASE_CURRENCY_KEY] = currencyId
        }
    }
}
