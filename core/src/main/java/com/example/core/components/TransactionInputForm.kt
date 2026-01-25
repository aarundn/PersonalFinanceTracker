package com.example.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.utils.parseDateString

@Composable
fun TransactionInputForm(
    isIncome: Boolean,
    modifier: Modifier = Modifier,
    onTypeChanged: (Boolean) -> Unit,
    selectedCategoryName: String,
    onCategorySelected: (String) -> Unit,
    onDateChanged:(String) -> Unit,
    onAmountChanged:(String) -> Unit,
    onNotesChanged:(String) -> Unit,
    onCurrencySelected: (String) -> Unit,
    onSave : () -> Unit,
    onCancel : () -> Unit,
    currency: String,
    isLoading : Boolean,
    amount: String,
    date: Long,
    notes: String,
    isSaveEnabled: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Card(
            modifier = Modifier.padding(top = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
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


                TransactionTypeToggle(
                    isIncome = isIncome,
                    onTypeChanged = onTypeChanged,
                )

                CategorySelector(
                    selectedCategory = selectedCategoryName,
                    isIncome = isIncome,
                    onCategorySelected = onCategorySelected
                )

                TransactionDropdown(
                    label = "Currency",
                    items = SUPPORTED_CURRENCIES.map { "${it.first} (${it.second})" },
                    selectedItem = currency.ifEmpty {
                        "select currency"
                    },
                    onItemSelected = onCurrencySelected,
                )
                FormInput(
                    label = "Amount ($currency)",
                    value = amount,
                    placeholder = "Enter amount",
                    onValueChange = onAmountChanged,
                )

                FormInput(
                    label = "Date",
                    value = parseDateString(date),
                    onValueChange = onDateChanged,
                    placeholder = "Select date"
                )

                FormInput(
                    label = "Notes (Optional)",
                    value = notes,
                    maxLine = 5,
                    minLine = 3,
                    onValueChange = onNotesChanged,
                    placeholder = "Add any additional notes..."
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

@Preview
@Composable
private fun TransactionInputFormPreview() {
    PersonalFinanceTrackerTheme {

    }
}