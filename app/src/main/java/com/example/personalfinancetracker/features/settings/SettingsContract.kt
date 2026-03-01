package com.example.personalfinancetracker.features.settings

import androidx.compose.runtime.Immutable
import com.example.core.common.MVIState
import com.example.core.common.MVIUiEvent
import com.example.core.common.MVIUiSideEffect
import com.example.core.model.Currency

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
) : MVIState

sealed interface SettingsEvent : MVIUiEvent {
    data object OnClickCurrencyPicker : SettingsEvent
    data object OnDismissCurrencyPicker : SettingsEvent
    data class OnCurrencySelected(val currency: Currency) : SettingsEvent
    data object OnNavigateBack : SettingsEvent
    // Conversion test events
    data class OnAmountChanged(val amount: String) : SettingsEvent
    data class OnTargetCurrencySelected(val currency: Currency) : SettingsEvent
    data class OnProviderSelected(val providerId: String) : SettingsEvent
    data object OnConvertClicked : SettingsEvent
}

sealed interface SettingsSideEffect : MVIUiSideEffect {
    data object NavigateBack : SettingsSideEffect
}
