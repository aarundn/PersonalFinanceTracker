package com.example.personalfinancetracker.features.settings

import androidx.compose.runtime.Immutable
import com.example.core.model.Currency

object SettingsContract {

    @Immutable
    data class SettingsState(
        val selectedCurrency: Currency,
        val availableCurrencies: List<Currency>,
        val isCurrencyPickerVisible: Boolean = false
    )

    sealed interface Event {
        data object OnClickCurrencyPicker : Event
        data object OnDismissCurrencyPicker : Event
        data class OnCurrencySelected(val currency: Currency) : Event
        data object OnNavigateBack : Event
    }

    sealed interface SideEffect {
        data object NavigateBack : SideEffect
    }
}
