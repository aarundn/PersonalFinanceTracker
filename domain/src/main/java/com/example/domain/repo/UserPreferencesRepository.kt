package com.example.domain.repo

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val baseCurrency: Flow<String>
    val selectedProviderId: Flow<String>
    suspend fun setBaseCurrency(currencyId: String)
    suspend fun setSelectedProviderId(providerId: String)
}
