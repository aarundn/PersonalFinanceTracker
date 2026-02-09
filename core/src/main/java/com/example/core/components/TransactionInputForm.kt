package com.example.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.model.Category
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.utils.SUPPORTED_CURRENCIES

@Composable
fun TransactionInputForm(
    categories: List<Category>,
    isIncome: Boolean,
    modifier: Modifier = Modifier,
    onTypeChanged: (Boolean) -> Unit,
    selectedCategoryName: String, // This is actually the ID now
    onCategorySelected: (String) -> Unit,
    onDateChanged: (String) -> Unit,
    onAmountChanged: (String) -> Unit,
    onNotesChanged: (String) -> Unit,
    onCurrencySelected: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    selectedCurrency: String,
    isLoading: Boolean,
    amount: String,
    date: String,
    notes: String,
    isSaveEnabled: Boolean,
    isReadOnly: Boolean = false,
    showTypeToggle: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TransactionHeaderForm()

                if (showTypeToggle) {
                    TransactionTypeToggle(
                        isIncome = isIncome,
                        onTypeChanged = onTypeChanged,
                    )
                }

                // Map Category ID to Localized Name
                val categoryMap = categories.associate { stringResource(it.nameResId) to it.id }
                val displayNames = categoryMap.keys.toList()
                
                // Find display name for selected ID
                val selectedDisplayName = categories.find { it.id == selectedCategoryName }?.let { stringResource(it.nameResId) } ?: ""

                TransactionDropdown(
                    label = "Category",
                    items = displayNames,
                    selectedItem = selectedDisplayName,
                    onItemSelected = { name -> 
                        val id = categoryMap[name] ?: ""
                        onCategorySelected(id) 
                    },
                    enabled = !isReadOnly
                )

                TransactionDropdown(
                    label = "Currency",
                    items = SUPPORTED_CURRENCIES.map { "${it.first} (${it.second})" },
                    selectedItem = selectedCurrency,
                    onItemSelected = onCurrencySelected,
                    enabled = !isReadOnly
                )
                FormInput(
                    label = "Amount ($selectedCurrency)",
                    value = amount,
                    placeholder = "Enter amount",
                    onValueChange = onAmountChanged,
                    enabled = !isReadOnly
                )

                FormInput(
                    label = "Date",
                    value = date,
                    onValueChange = onDateChanged,
                    placeholder = "Select date",
                    enabled = !isReadOnly
                )

                FormInput(
                    label = "Notes (Optional)",
                    value = notes,
                    maxLine = 5,
                    minLine = 3,
                    onValueChange = onNotesChanged,
                    placeholder = "Add any additional notes...",
                    enabled = !isReadOnly
                )
            }
        }

        ActionButtons(
            onCancel = onCancel,
            onSave = onSave,
            isLoading = isLoading,
            isSaveEnabled = isSaveEnabled
        )
    }
}

@Composable
private fun TransactionHeaderForm() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            Icons.Outlined.Add,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Transaction Details",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
private fun TransactionInputFormPreview() {
    PersonalFinanceTrackerTheme {
        TransactionInputForm(
            categories = emptyList(),
            isIncome = true,
            onTypeChanged = {},
            selectedCategoryName = "",
            onCategorySelected = {},
            onDateChanged = {},
            onAmountChanged = {},
            onNotesChanged = {},
            onCurrencySelected = {},
            onSave = {},
            onCancel = {},
            selectedCurrency = "",
            isLoading = false,
            amount = "",
            date = "",
            notes = "",
            isSaveEnabled = false,
        )
    }
}