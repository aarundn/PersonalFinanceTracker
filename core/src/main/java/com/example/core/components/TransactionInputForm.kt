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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.R
import com.example.core.model.Category
import com.example.core.model.Currency
import com.example.core.model.DefaultCurrencies
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.model.BudgetDisplayData
import com.example.core.ui.theme.dimensions

@Composable
fun TransactionInputForm(
    categories: List<Category>,
    isIncome: Boolean,
    modifier: Modifier = Modifier,
    onTypeChanged: (Boolean) -> Unit,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit,
    onAmountChanged: (String) -> Unit,
    onNotesChanged: (String) -> Unit,
    onCurrencySelected: (Currency) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    selectedCurrency: Currency?,
    isLoading: Boolean,
    amount: String,
    date: String,
    onDatePickerClicked: () -> Unit,
    notes: String,
    isReadOnly: Boolean = false,
    showTypeToggle: Boolean = true,
    selectedBudget: BudgetDisplayData? = null,
    onLinkBudgetClicked: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.spacingMedium)
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            border = BorderStroke(MaterialTheme.dimensions.borderThin, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier.padding(MaterialTheme.dimensions.spacingMedium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.spacingMedium)
            ) {
                TransactionHeaderForm()

                if (showTypeToggle) {
                    TransactionTypeToggle(
                        isIncome = isIncome,
                        onTypeChanged = onTypeChanged,
                    )
                }

                TransactionDropdown(
                    label = stringResource(R.string.form_label_category),
                    items = categories,
                    selectedItem = selectedCategory,
                    onItemSelected = onCategorySelected,
                    itemLabel = { stringResource(it.nameResId) },
                    enabled = !isReadOnly
                )
                if (!isIncome && selectedCategory != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (selectedBudget != null) {
                            BudgetSelectorCard(
                                budget = selectedBudget,
                                isSelected = true,
                                onClick = onLinkBudgetClicked,
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.form_no_budget_linked),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        TextButton(onClick = onLinkBudgetClicked) {
                            Text(
                                if (selectedBudget != null) stringResource(R.string.form_change_budget) else stringResource(
                                    R.string.form_link_budget
                                )
                            )
                        }
                    }
                }

                TransactionDropdown(
                    label = stringResource(R.string.form_label_currency),
                    items = DefaultCurrencies.all,
                    selectedItem = selectedCurrency,
                    onItemSelected = onCurrencySelected,
                    itemLabel = { "${it.id} (${it.symbol})" },
                    enabled = !isReadOnly
                )
                FormInput(
                    label = stringResource(R.string.form_label_amount) + " (${selectedCurrency?.symbol ?: ""})",
                    value = amount,
                    placeholder = stringResource(R.string.form_placeholder_amount),
                    onValueChange = onAmountChanged,
                    enabled = !isReadOnly
                )

                FormInput(
                    label = stringResource(R.string.form_label_date),
                    value = date,
                    onValueChange = {},
                    placeholder = stringResource(R.string.form_placeholder_date),
                    enabled = !isReadOnly,
                    onClick = { if (!isReadOnly) onDatePickerClicked() }
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
        )
    }
}

@Composable
private fun TransactionHeaderForm() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.spacingSmall)
    ) {
        Icon(
            Icons.Outlined.Add,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = stringResource(R.string.form_transaction_details),
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
            selectedCategory = null,
            onCategorySelected = {},
            onAmountChanged = {},
            onNotesChanged = {},
            onCurrencySelected = {},
            onSave = {},
            onCancel = {},
            selectedCurrency = null,
            isLoading = false,
            amount = "",
            date = "",
            onDatePickerClicked = {},
            notes = "",
        )
    }
}