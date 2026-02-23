package com.example.domain.repo

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val baseCurrency: Flow<String>
    suspend fun setBaseCurrency(currencyId: String)
}
