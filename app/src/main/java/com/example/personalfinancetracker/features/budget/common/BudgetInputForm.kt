package com.example.personalfinancetracker.features.budget.common

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.components.ActionButtons
import com.example.core.components.FormInput
import com.example.core.components.TransactionDropdown
import com.example.core.model.Category
import com.example.core.model.Currency
import com.example.core.model.DefaultCategories
import com.example.core.model.DefaultCurrencies
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.domain.model.BudgetPeriod

@Composable
fun BudgetInputForm(
    categories: List<Category>,
    selectedCategory: Category?,
    selectedPeriod: BudgetPeriod,
    amount: String,
    notes: String,
    selectedCurrency: Currency?,
    modifier: Modifier = Modifier,
    onCategorySelected: (Category) -> Unit,
    onPeriodSelected: (BudgetPeriod) -> Unit,
    onAmountChanged: (String) -> Unit,
    onCurrencySelected: (Currency) -> Unit,
    onNotesChanged: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    isLoading: Boolean = false,
    isReadOnly: Boolean = false,
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
                BudgetHeaderForm()

                TransactionDropdown(
                    label = "Category",
                    items = categories,
                    selectedItem = selectedCategory,
                    onItemSelected = onCategorySelected,
                    itemLabel = { stringResource(it.nameResId) },
                    enabled = !isReadOnly
                )

                TransactionDropdown(
                    label = "Currency",
                    items = DefaultCurrencies.all,
                    selectedItem = selectedCurrency,
                    onItemSelected = onCurrencySelected,
                    itemLabel = { "${it.id} (${it.symbol})" },
                    enabled = !isReadOnly
                )

                FormInput(
                    label = "Budget Amount (${selectedCurrency?.symbol ?: ""})",
                    value = amount,
                    placeholder = "Enter budget amount",
                    onValueChange = onAmountChanged,
                    keyboardType = KeyboardType.Decimal,
                    enabled = !isReadOnly
                )

                TransactionDropdown(
                    label = "Budget Period",
                    items = BudgetPeriod.entries,
                    selectedItem = selectedPeriod,
                    onItemSelected = onPeriodSelected ,
                    itemLabel = { "${it.label} (${it.days} days)" },
                    enabled = !isReadOnly
                )

                FormInput(
                    label = "Notes (Optional)",
                    value = notes,
                    maxLine = 5,
                    minLine = 3,
                    onValueChange = onNotesChanged,
                    placeholder = "Add any notes about this budget...",
                    enabled = !isReadOnly
                )
            }
        }

        if (!isReadOnly) {
            ActionButtons(
                onCancel = onCancel,
                onSave = onSave,
                isLoading = isLoading,
            )
        }
    }
}

@Composable
private fun BudgetHeaderForm() {
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
            text = "Budget Details",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BudgetInputFormPreview() {
    PersonalFinanceTrackerTheme {
        BudgetInputForm(
            categories = DefaultCategories.getCategories(isIncome = false),
            selectedCategory = DefaultCategories.FOOD,
            selectedPeriod = BudgetPeriod.Monthly,
            amount = "500.00",
            notes = "",
            selectedCurrency = DefaultCurrencies.USD,
            onCategorySelected = {},
            onPeriodSelected = {},
            onAmountChanged = {},
            onCurrencySelected = {},
            onNotesChanged = {},
            onSave = {},
            onCancel = {},
        )
    }
}
