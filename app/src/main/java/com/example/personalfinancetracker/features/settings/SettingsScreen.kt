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
import androidx.compose.ui.res.stringResource
import com.example.personalfinancetracker.R
import com.example.core.components.HeaderSection
import com.example.core.components.TransactionDropdown
import com.example.personalfinancetracker.features.settings.components.ConversionTestCard
import com.example.personalfinancetracker.features.settings.components.SyncStatusBanner

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
                title = stringResource(R.string.settings_title)
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
                text = stringResource(R.string.settings_general_section),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            SyncStatusBanner(
                syncStatus = state.syncStatus,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            TransactionDropdown(
                label = stringResource(R.string.settings_base_currency),
                items = state.availableCurrencies,
                selectedItem = state.selectedCurrency,
                onItemSelected = { onEvent(SettingsEvent.OnCurrencySelected(it)) },
                itemLabel = { "${it.symbol} (${it.id}) - ${it.name}" }
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
                conversionError = state.conversionError?.asString()
            )
        }
    }

}
