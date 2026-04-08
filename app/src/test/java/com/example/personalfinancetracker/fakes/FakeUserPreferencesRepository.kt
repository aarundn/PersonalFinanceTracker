package com.example.personalfinancetracker.fakes

import com.example.domain.repo.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserPreferencesRepository : UserPreferencesRepository {

    private val _baseCurrency = MutableStateFlow("USD")
    override val baseCurrency: Flow<String> = _baseCurrency

    private val _selectedProviderId = MutableStateFlow("provider1")
    override val selectedProviderId: Flow<String> = _selectedProviderId

    override suspend fun setBaseCurrency(currencyId: String) {
        _baseCurrency.value = currencyId
    }

    override suspend fun setSelectedProviderId(providerId: String) {
        _selectedProviderId.value = providerId
    }
}
