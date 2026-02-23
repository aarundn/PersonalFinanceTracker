package com.example.personalfinancetracker.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.model.Currency
import com.example.core.model.DefaultCurrencies
import com.example.domain.repo.UserPreferencesRepository
import com.example.personalfinancetracker.features.settings.SettingsContract.Event
import com.example.personalfinancetracker.features.settings.SettingsContract.SettingsState
import com.example.personalfinancetracker.features.settings.SettingsContract.SideEffect
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    private val _sideEffect = MutableSharedFlow<SideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private val _pickerVisible = MutableStateFlow(false)

    val state: StateFlow<SettingsState> =
        combine(
            userPreferencesRepository.baseCurrency,
            _pickerVisible,
        ) { currencyId, pickerVisible ->
            SettingsState(
                selectedCurrency = DefaultCurrencies.fromId(currencyId) ?: DefaultCurrencies.USD,
                availableCurrencies = DefaultCurrencies.all,
                isCurrencyPickerVisible = pickerVisible,
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SettingsState(
                    selectedCurrency = DefaultCurrencies.USD,
                    availableCurrencies = DefaultCurrencies.all,
                )
            )

    fun onEvent(event: Event) {
        when (event) {
            Event.OnClickCurrencyPicker -> _pickerVisible.value = true
            Event.OnDismissCurrencyPicker -> _pickerVisible.value = false
            is Event.OnCurrencySelected -> selectCurrency(event.currency)
            Event.OnNavigateBack -> viewModelScope.launch {
                _sideEffect.emit(SideEffect.NavigateBack)
            }
        }
    }

    private fun selectCurrency(currency: Currency) {
        viewModelScope.launch {
            userPreferencesRepository.setBaseCurrency(currency.id)
            _pickerVisible.value = false
        }
    }
}
