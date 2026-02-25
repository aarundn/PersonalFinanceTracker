package com.example.personalfinancetracker.features.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.core.components.HeaderSection
import com.example.core.model.Currency
import com.example.personalfinancetracker.features.settings.SettingsContract.Event
import com.example.personalfinancetracker.features.settings.SettingsContract.SettingsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: SettingsState,
    onEvent: (Event) -> Unit,
) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            HeaderSection(
                onBackClick = { onEvent(Event.OnNavigateBack) },title = "Settings",)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Base Currency Section
            Text(
                "General",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEvent(Event.OnClickCurrencyPicker) },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Base Currency",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            state.selectedCurrency.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        "${state.selectedCurrency.symbol} (${state.selectedCurrency.id})",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Conversion Test Section
            Text(
                "Currency Conversion Test",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            // Provider picker
            if (state.availableProviders.isNotEmpty()) {
                ProviderDropdown(
                    providers = state.availableProviders,
                    selectedProviderId = state.selectedProviderId,
                    onSelected = { onEvent(Event.OnProviderSelected(it)) }
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // From
                    Text(
                        "From: ${state.selectedCurrency.symbol} (${state.selectedCurrency.id})",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    // Amount
                    OutlinedTextField(
                        value = state.conversionAmount,
                        onValueChange = { onEvent(Event.OnAmountChanged(it)) },
                        label = { Text("Amount") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Target currency dropdown
                    TargetCurrencyDropdown(
                        currencies = state.availableCurrencies.filter {
                            it.id != state.selectedCurrency.id
                        },
                        selected = state.targetCurrency,
                        onSelected = { onEvent(Event.OnTargetCurrencySelected(it)) }
                    )

                    Button(
                        onClick = { onEvent(Event.OnConvertClicked) },
                        enabled = !state.isConverting
                                && state.targetCurrency != null
                                && state.selectedProviderId != null
                                && state.conversionAmount.toBigDecimalOrNull() != null,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (state.isConverting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Convert")
                        }
                    }

                    // Result
                    state.conversionResult?.let { result ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Text(
                                text = "${state.conversionAmount} ${state.selectedCurrency.id} = $result ${state.targetCurrency?.id ?: ""}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }

                    // Error
                    state.conversionError?.let { error ->
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }

    // Currency picker bottom sheet
    if (state.isCurrencyPickerVisible) {
        CurrencyPickerBottomSheet(
            currencies = state.availableCurrencies,
            selectedCurrency = state.selectedCurrency,
            onCurrencySelected = { onEvent(Event.OnCurrencySelected(it)) },
            onDismiss = { onEvent(Event.OnDismissCurrencyPicker) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TargetCurrencyDropdown(
    currencies: List<Currency>,
    selected: Currency?,
    onSelected: (Currency) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(
            value = selected?.let { "${it.symbol} ${it.id}" } ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("To Currency") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            currencies.forEach { currency ->
                DropdownMenuItem(
                    text = { Text("${currency.symbol} ${currency.id} — ${currency.name}") },
                    onClick = {
                        onSelected(currency)
                        expanded = false
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProviderDropdown(
    providers: List<Pair<String, String>>,
    selectedProviderId: String?,
    onSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedName = providers.firstOrNull { it.first == selectedProviderId }?.second ?: ""

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(
            value = selectedName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Provider") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            providers.forEach { (id, displayName) ->
                DropdownMenuItem(
                    text = { Text(displayName) },
                    onClick = {
                        onSelected(id)
                        expanded = false
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencyPickerBottomSheet(
    currencies: List<Currency>,
    selectedCurrency: Currency,
    onCurrencySelected: (Currency) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Text(
            "Select Base Currency",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(8.dp))
        LazyColumn {
            items(currencies, key = { it.id }) { currency ->
                CurrencyItem(
                    currency = currency,
                    isSelected = currency.id == selectedCurrency.id,
                    onClick = { onCurrencySelected(currency) }
                )
            }
        }
    }
}

@Composable
private fun CurrencyItem(
    currency: Currency,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                currency.symbol,
                style = MaterialTheme.typography.titleMedium,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface
            )
            Column {
                Text(
                    currency.id,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    currency.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (isSelected) {
            Icon(
                Icons.Outlined.Check,
                contentDescription = "Selected",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
