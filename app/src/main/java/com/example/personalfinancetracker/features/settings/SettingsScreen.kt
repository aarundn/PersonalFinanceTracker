package com.example.personalfinancetracker.features.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.core.components.HeaderSection
import com.example.personalfinancetracker.features.settings.components.BaseCurrencyCard
import com.example.personalfinancetracker.features.settings.components.ConversionTestCard
import com.example.personalfinancetracker.features.settings.components.CurrencyPickerBottomSheet

@Composable
fun SettingsScreen(
    state: SettingsState,
    onEvent: (SettingsEvent) -> Unit,
) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            HeaderSection(
                onBackClick = { onEvent(SettingsEvent.OnNavigateBack) },
                title = "Settings"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = "General",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            BaseCurrencyCard(
                selectedCurrency = state.selectedCurrency,
                onClick = { onEvent(SettingsEvent.OnClickCurrencyPicker) }
            )
            
            ConversionTestCard(
                baseCurrency = state.selectedCurrency,
                conversionAmount = state.conversionAmount,
                onAmountChanged = { onEvent(SettingsEvent.OnAmountChanged(it)) },
                availableCurrencies = state.availableCurrencies,
                targetCurrency = state.targetCurrency,
                onTargetCurrencySelected = { onEvent(SettingsEvent.OnTargetCurrencySelected(it)) },
                availableProviders = state.availableProviders,
                selectedProviderId = state.selectedProviderId,
                onProviderSelected = { onEvent(SettingsEvent.OnProviderSelected(it)) },
                isConverting = state.isConverting,
                onConvertClicked = { onEvent(SettingsEvent.OnConvertClicked) },
                conversionResult = state.conversionResult,
                conversionError = state.conversionError
            )
        }
    }

    if (state.isCurrencyPickerVisible) {
        CurrencyPickerBottomSheet(
            currencies = state.availableCurrencies,
            selectedCurrency = state.selectedCurrency,
            onCurrencySelected = { onEvent(SettingsEvent.OnCurrencySelected(it)) },
            onDismiss = { onEvent(SettingsEvent.OnDismissCurrencyPicker) }
        )
    }
}
