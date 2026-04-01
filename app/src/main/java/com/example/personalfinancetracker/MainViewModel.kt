package com.example.personalfinancetracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conversion_rate.domain.usecase.SchedulePeriodicRateSyncUseCase
import com.example.data.sync.DataSyncManager
import com.example.domain.repo.UserPreferencesRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn

class MainViewModel(
    private val schedulePeriodicRateSyncUseCase: SchedulePeriodicRateSyncUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val dataSyncManager: DataSyncManager
) : ViewModel() {

    init {
        dataSyncManager.schedulePeriodicSync()
        combine(
            userPreferencesRepository.baseCurrency,
            userPreferencesRepository.selectedProviderId
        ) { baseCurrency, providerId ->
            schedulePeriodicRateSyncUseCase(baseCurrency, providerId)
        }.launchIn(viewModelScope)
    }
}
