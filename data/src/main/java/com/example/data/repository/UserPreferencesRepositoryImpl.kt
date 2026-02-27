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
        val SELECTED_PROVIDER_KEY = stringPreferencesKey("selected_provider_id")
        const val DEFAULT_CURRENCY = "DZD"
        const val DEFAULT_PROVIDER = "exchangerate_api"
    }

    override val baseCurrency: Flow<String> =
        context.dataStore.data.map { prefs ->
            prefs[BASE_CURRENCY_KEY] ?: DEFAULT_CURRENCY
        }

    override val selectedProviderId: Flow<String> =
        context.dataStore.data.map { prefs ->
            prefs[SELECTED_PROVIDER_KEY] ?: DEFAULT_PROVIDER
        }

    override suspend fun setBaseCurrency(currencyId: String) {
        context.dataStore.edit { prefs ->
            prefs[BASE_CURRENCY_KEY] = currencyId
        }
    }

    override suspend fun setSelectedProviderId(providerId: String) {
        context.dataStore.edit { prefs ->
            prefs[SELECTED_PROVIDER_KEY] = providerId
        }
    }
}
