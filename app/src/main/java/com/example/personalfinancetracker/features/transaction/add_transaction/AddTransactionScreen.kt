package com.example.personalfinancetracker.features.transaction.add_transaction

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import com.example.personalfinancetracker.features.transaction.add_transaction.components.HeaderSection
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.personalfinancetracker.features.transaction.add_transaction.components.ActionButtons
import com.example.personalfinancetracker.features.transaction.add_transaction.components.AmountInput
import com.example.personalfinancetracker.features.transaction.add_transaction.components.CategorySelector
import com.example.personalfinancetracker.features.transaction.add_transaction.components.CurrencySelector
import com.example.personalfinancetracker.features.transaction.add_transaction.components.FormInput
import com.example.personalfinancetracker.features.transaction.add_transaction.components.FormTextArea
import com.example.personalfinancetracker.features.transaction.add_transaction.components.TransactionTypeToggle
import com.example.personalfinancetracker.ui.theme.PersonalFinanceTrackerTheme

@Composable
fun AddTransactionScreen(
    state: AddTransactionContract.State,
    onEvent: (AddTransactionContract.Event) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            HeaderSection(
                onBackClick = { onEvent(AddTransactionContract.Event.OnCancel) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Transaction Details Card
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

                    // Transaction Type Toggle
                    TransactionTypeToggle(
                        isIncome = state.isIncome,
                        onTypeChanged = {
                            onEvent(
                                AddTransactionContract.Event.OnTransactionTypeChanged(
                                    it
                                )
                            )
                        }
                    )

                    // Title Input
                    FormInput(
                        label = "Title",
                        value = state.title,
                        onValueChange = { onEvent(AddTransactionContract.Event.OnTitleChanged(it)) },
                        placeholder = "Enter transaction title"
                    )

                    // Category Selector
                    CategorySelector(
                        selectedCategory = state.category,
                        isIncome = state.isIncome,
                        onCategorySelected = {
                            onEvent(
                                AddTransactionContract.Event.OnCategoryChanged(
                                    it
                                )
                            )
                        }
                    )

                    // Currency Selection
                    CurrencySelector(
                        selectedCurrency = state.currency,
                        onCurrencySelected = {
                            onEvent(
                                AddTransactionContract.Event.OnCurrencyChanged(
                                    it
                                )
                            )
                        }
                    )

                    // Amount Input
                    AmountInput(
                        amount = state.amount,
                        currency = state.currency,
                        convertedAmount = state.convertedAmount,
                        isConverting = state.isConverting,
                        onAmountChanged = { onEvent(AddTransactionContract.Event.OnAmountChanged(it)) },
                        onConvertCurrency = { onEvent(AddTransactionContract.Event.OnConvertCurrency) }
                    )

                    // Date Input
                    FormInput(
                        label = "Date",
                        value = state.date,
                        onValueChange = { onEvent(AddTransactionContract.Event.OnDateChanged(it)) },
                        placeholder = "Select date"
                    )

                    // Notes Input
                    FormTextArea(
                        label = "Notes (Optional)",
                        value = state.notes,
                        onValueChange = { onEvent(AddTransactionContract.Event.OnNotesChanged(it)) },
                        placeholder = "Add any additional notes..."
                    )
                }
            }

            // Action Buttons
            ActionButtons(
                onCancel = { onEvent(AddTransactionContract.Event.OnCancel) },
                onSave = { onEvent(AddTransactionContract.Event.OnSave) },
                isLoading = state.isLoading,
                isSaveEnabled = state.title.isNotEmpty() &&
                        state.category.isNotEmpty() &&
                        state.amount.isNotEmpty()
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun AddTransactionScreenPreview() {
    PersonalFinanceTrackerTheme {
        AddTransactionScreen(
            state = AddTransactionContract.State(
                isIncome = false,
                title = "Sample Transaction",
                category = "Food",
                amount = "25.50",
                currency = "USD",
                date = "2024-01-15",
                notes = "Lunch at restaurant"
            ),
            onEvent = {}
        )
    }
}