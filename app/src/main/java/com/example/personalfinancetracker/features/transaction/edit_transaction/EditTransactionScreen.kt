package com.example.personalfinancetracker.features.transaction.edit_transaction

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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.components.AmountInput
import com.example.core.components.CategorySelector
import com.example.core.components.CurrencySelector
import com.example.core.components.LoadingIndicator
import com.example.core.components.PaymentMethodSelector
import com.example.core.components.FormInput
import com.example.core.components.TransactionTypeToggle
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.personalfinancetracker.features.transaction.edit_transaction.components.ActionButtons
import com.example.personalfinancetracker.features.transaction.edit_transaction.components.HeaderSection
import com.example.personalfinancetracker.features.transaction.edit_transaction.components.TransactionOverviewCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen(
    state: EditTransactionContract.State,
    onEvent: (EditTransactionContract.Event) -> Unit,
) {
    // Load transaction when screen is first displayed
    LaunchedEffect(Unit) {
        onEvent(EditTransactionContract.Event.OnLoadTransaction)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            HeaderSection(
                onBackClick = { onEvent(EditTransactionContract.Event.OnCancel) },
                title = if (state.isEditing) "Edit Transaction" else "Transaction Details",
                actions = {
                    if (!state.isEditing) {
                        IconButton(onClick = { onEvent(EditTransactionContract.Event.OnEdit) }) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "Edit"
                            )
                        }
                    }
                }
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
            // Loading indicator
            if (state.isLoading) {
                LoadingIndicator(modifier = Modifier.padding(vertical = 32.dp))
            } else {
                // Transaction Overview Card
                TransactionOverviewCard(
                    state = state,
                    modifier = Modifier.padding(top = 16.dp)
                )
                
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
                        // Card Header (only show when editing)
                        if (state.isEditing) {
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
                                        Icons.Outlined.Edit,
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
                        }
                        // Transaction Type Toggle (only show when editing)
                        if (state.isEditing) {
                            TransactionTypeToggle(
                                isIncome = state.isIncome,
                                onTypeChanged = { onEvent(EditTransactionContract.Event.OnTransactionTypeChanged(it)) }
                            )
                        }

                        // Title Input
                        FormInput(
                            label = "Title",
                            value = state.title,
                            onValueChange = { onEvent(EditTransactionContract.Event.OnTitleChanged(it)) },
                            placeholder = "Enter transaction title",
                            modifier = if (!state.isEditing) Modifier else Modifier
                        )

                        // Category Selector
                        if (state.isEditing) {
                            CategorySelector(
                                isIncome = state.isIncome,
                                selectedCategory = state.category,
                                onCategorySelected = { onEvent(EditTransactionContract.Event.OnCategoryChanged(it)) }
                            )
                        } else {
                            FormInput(
                                label = "Category",
                                value = state.category,
                                onValueChange = {},
                                placeholder = "Category",
                                modifier = Modifier
                            )
                        }

                        // Currency Selection (only show when editing)
                        if (state.isEditing) {
                            CurrencySelector(
                                selectedCurrency = state.currency,
                                onCurrencySelected = { onEvent(EditTransactionContract.Event.OnCurrencyChanged(it)) }
                            )
                        }

                        // Amount Input
                        if (state.isEditing) {
                            AmountInput(
                                amount = state.amount,
                                currency = state.currency,
                                convertedAmount = state.convertedAmount,
                                isConverting = state.isConverting,
                                onAmountChanged = { onEvent(EditTransactionContract.Event.OnAmountChanged(it)) },
                                onConvertCurrency = { onEvent(EditTransactionContract.Event.OnConvertCurrency) }
                            )
                        } else {
                            FormInput(
                                label = "Amount",
                                value = "${if (state.isIncome) "+" else "-"}$${state.amount}",
                                onValueChange = {},
                                placeholder = "Amount",
                                modifier = Modifier
                            )
                        }

                        // Date Input
                        FormInput(
                            label = "Date",
                            value = state.date,
                            onValueChange = { onEvent(EditTransactionContract.Event.OnDateChanged(it)) },
                            placeholder = "Select date",
                            modifier = if (!state.isEditing) Modifier else Modifier
                        )

                        // Location Input (only show when editing)
                        if (state.isEditing) {
                            FormInput(
                                label = "Location (Optional)",
                                value = state.location,
                                onValueChange = { onEvent(EditTransactionContract.Event.OnLocationChanged(it)) },
                                placeholder = "Where was this transaction made?"
                            )
                        } else if (state.location.isNotEmpty()) {
                            FormInput(
                                label = "Location",
                                value = state.location,
                                onValueChange = {},
                                placeholder = "Location",
                                modifier = Modifier
                            )
                        }

                        // Payment Method Selector (only show when editing)
                        if (state.isEditing) {
                            PaymentMethodSelector(
                                selectedPaymentMethod = state.paymentMethod,
                                onPaymentMethodSelected = { onEvent(EditTransactionContract.Event.OnPaymentMethodChanged(it)) }
                            )
                        } else if (state.paymentMethod.isNotEmpty()) {
                            FormInput(
                                label = "Payment Method",
                                value = state.paymentMethod,
                                onValueChange = {},
                                placeholder = "Payment Method",
                                modifier = Modifier
                            )
                        }

                        // Notes Input
                        FormInput(
                            label = "Notes (Optional)",
                            maxLine = 5,
                            minLine = 3,
                            value = state.notes,
                            onValueChange = { onEvent(EditTransactionContract.Event.OnNotesChanged(it)) },
                            placeholder = "Add any additional notes...",
                            modifier = if (!state.isEditing) Modifier else Modifier
                        )
                    }
                }

                // Action Buttons
                if (state.isEditing) {
                    ActionButtons(
                        onCancel = { onEvent(EditTransactionContract.Event.OnCancel) },
                        onSave = { onEvent(EditTransactionContract.Event.OnSave) },
                        isLoading = false,
                        isSaveEnabled = state.title.isNotEmpty() && 
                                state.category.isNotEmpty() && 
                                state.amount.isNotEmpty()
                    )
                }

                // Delete Button (only show when not editing)
                if (!state.isEditing) {
                    Button(
                        onClick = { onEvent(EditTransactionContract.Event.OnDelete) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFDC2626), // Red-600
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Delete Transaction")
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun EditTransactionScreenPreview() {
    PersonalFinanceTrackerTheme {
        EditTransactionScreen(
            state = EditTransactionContract.State(
                transactionId = 1,
                isIncome = false,
                title = "Grocery Shopping",
                category = "Food",
                amount = "85.50",
                currency = "USD",
                date = "2024-03-15",
                notes = "Weekly grocery shopping at Whole Foods",
                location = "Whole Foods Market",
                paymentMethod = "Credit Card",
                isEditing = false,
                icon = Icons.Outlined.ShoppingCart,
                iconTint = Color(0xFFF97316)
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditTransactionScreenEditingPreview() {
    PersonalFinanceTrackerTheme {
        EditTransactionScreen(
            state = EditTransactionContract.State(
                transactionId = 2,
                isIncome = true,
                title = "Freelance Work",
                category = "Income",
                amount = "800.00",
                currency = "USD",
                date = "2024-03-12",
                notes = "Website development project",
                location = "",
                paymentMethod = "Bank Transfer",
                isEditing = true,
                icon = Icons.Outlined.Home,
                iconTint = Color(0xFF22C55E)
            ),
            onEvent = {}
        )
    }
}
