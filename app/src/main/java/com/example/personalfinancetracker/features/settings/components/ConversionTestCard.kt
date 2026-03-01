package com.example.personalfinancetracker.features.settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.core.components.FormInput
import com.example.core.components.TransactionDropdown
import com.example.core.model.Currency

@Composable
fun ConversionTestCard(
    baseCurrency: Currency,
    conversionAmount: String,
    onAmountChanged: (String) -> Unit,
    availableCurrencies: List<Currency>,
    targetCurrency: Currency?,
    onTargetCurrencySelected: (Currency) -> Unit,
    availableProviders: List<Pair<String, String>>,
    selectedProviderId: String?,
    onProviderSelected: (String) -> Unit,
    isConverting: Boolean,
    onConvertClicked: () -> Unit,
    conversionResult: String?,
    conversionError: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Currency Conversion Test",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        // Provider picker
        if (availableProviders.isNotEmpty()) {
            TransactionDropdown(
                label = "Exchange Rate Provider",
                items = availableProviders,
                selectedItem = availableProviders.firstOrNull { it.first == selectedProviderId },
                onItemSelected = { onProviderSelected(it.first) },
                itemLabel = { it.second }
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // From
                Text(
                    text = "From: ${baseCurrency.symbol} (${baseCurrency.id})",
                    style = MaterialTheme.typography.bodyMedium
                )

                // Amount
                FormInput(
                    label = "Amount",
                    value = conversionAmount,
                    onValueChange = onAmountChanged,
                    keyboardType = KeyboardType.Decimal
                )

                // Target currency dropdown
                TransactionDropdown(
                    label = "To Currency",
                    items = availableCurrencies.filter { it.id != baseCurrency.id },
                    selectedItem = targetCurrency,
                    onItemSelected = onTargetCurrencySelected,
                    itemLabel = { "${it.symbol} ${it.id} — ${it.name}" }
                )

                Button(
                    onClick = onConvertClicked,
                    enabled = !isConverting
                            && targetCurrency != null
                            && selectedProviderId != null
                            && conversionAmount.toBigDecimalOrNull() != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isConverting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Convert")
                    }
                }

                // Result
                conversionResult?.let { result ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            text = "$conversionAmount ${baseCurrency.id} = $result ${targetCurrency?.id ?: ""}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                // Error
                conversionError?.let { error ->
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
