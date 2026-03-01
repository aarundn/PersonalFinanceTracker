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
import com.example.personalfinancetracker.features.settings.SettingsContract.Event
import com.example.personalfinancetracker.features.settings.SettingsContract.SettingsState
import com.example.personalfinancetracker.features.settings.components.BaseCurrencyCard
import com.example.personalfinancetracker.features.settings.components.ConversionTestCard
import com.example.personalfinancetracker.features.settings.components.CurrencyPickerBottomSheet

@Composable
fun SettingsScreen(
    state: SettingsState,
    onEvent: (Event) -> Unit,
) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            HeaderSection(
                onBackClick = { onEvent(Event.OnNavigateBack) },
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
                onClick = { onEvent(Event.OnClickCurrencyPicker) }
            )
            
            ConversionTestCard(
                baseCurrency = state.selectedCurrency,
                conversionAmount = state.conversionAmount,
                onAmountChanged = { onEvent(Event.OnAmountChanged(it)) },
                availableCurrencies = state.availableCurrencies,
                targetCurrency = state.targetCurrency,
                onTargetCurrencySelected = { onEvent(Event.OnTargetCurrencySelected(it)) },
                availableProviders = state.availableProviders,
                selectedProviderId = state.selectedProviderId,
                onProviderSelected = { onEvent(Event.OnProviderSelected(it)) },
                isConverting = state.isConverting,
                onConvertClicked = { onEvent(Event.OnConvertClicked) },
                conversionResult = state.conversionResult,
                conversionError = state.conversionError
            )
        }
    }

    if (state.isCurrencyPickerVisible) {
        CurrencyPickerBottomSheet(
            currencies = state.availableCurrencies,
            selectedCurrency = state.selectedCurrency,
            onCurrencySelected = { onEvent(Event.OnCurrencySelected(it)) },
            onDismiss = { onEvent(Event.OnDismissCurrencyPicker) }
        )
    }
}
