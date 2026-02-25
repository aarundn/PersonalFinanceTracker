package com.example.personalfinancetracker.features.settings

import androidx.compose.runtime.Immutable
import com.example.core.model.Currency

object SettingsContract {

    @Immutable
    data class SettingsState(
        val selectedCurrency: Currency,
        val availableCurrencies: List<Currency>,
        val isCurrencyPickerVisible: Boolean = false,
        // Conversion test
        val conversionAmount: String = "100",
        val targetCurrency: Currency? = null,
        val conversionResult: String? = null,
        val isConverting: Boolean = false,
        val conversionError: String? = null,
        // Providers
        val availableProviders: List<Pair<String, String>> = emptyList(),
        val selectedProviderId: String? = null,
        val selectedProviderName: String? = null,
    )

    sealed interface Event {
        data object OnClickCurrencyPicker : Event
        data object OnDismissCurrencyPicker : Event
        data class OnCurrencySelected(val currency: Currency) : Event
        data object OnNavigateBack : Event
        // Conversion test events
        data class OnAmountChanged(val amount: String) : Event
        data class OnTargetCurrencySelected(val currency: Currency) : Event
        data class OnProviderSelected(val providerId: String) : Event
        data object OnConvertClicked : Event
    }

    sealed interface SideEffect {
        data object NavigateBack : SideEffect
    }
}
