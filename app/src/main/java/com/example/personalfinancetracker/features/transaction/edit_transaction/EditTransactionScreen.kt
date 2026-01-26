package com.example.personalfinancetracker.features.transaction.edit_transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.components.HeaderSection
import com.example.core.components.LoadingIndicator
import com.example.core.components.TransactionInputForm
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.utils.parseDateString
import com.example.personalfinancetracker.features.transaction.edit_transaction.components.TransactionOverviewCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen(
    state: EditTransactionContract.State,
    onEvent: (EditTransactionContract.Event) -> Unit,
) {
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
            if (state.isLoading) {
                LoadingIndicator(modifier = Modifier.padding(vertical = 32.dp))
            } else {
                TransactionOverviewCard(
                    state = state,
                    modifier = Modifier.padding(top = 16.dp)
                )

                // Transaction Details / Edit Form
                TransactionInputForm(
                    modifier = Modifier.padding(top = 16.dp),
                    isIncome = state.isIncome,
                    onTypeChanged = { if (state.isEditing) onEvent(EditTransactionContract.Event.OnTransactionTypeChanged(it)) },
                    selectedCategoryName = state.category,
                    onCategorySelected = { if (state.isEditing) onEvent(EditTransactionContract.Event.OnCategoryChanged(it)) },
                    onDateChanged = { if (state.isEditing) onEvent(EditTransactionContract.Event.OnDateChanged(it.toLong())) },
                    onAmountChanged = { if (state.isEditing) onEvent(EditTransactionContract.Event.OnAmountChanged(it)) },
                    onNotesChanged = { if (state.isEditing) onEvent(EditTransactionContract.Event.OnNotesChanged(it)) },
                    onCurrencySelected = { if (state.isEditing) onEvent(EditTransactionContract.Event.OnCurrencyChanged(it)) },
                    onSave = { onEvent(EditTransactionContract.Event.OnSave) },
                    onCancel = { onEvent(EditTransactionContract.Event.OnCancel) },
                    selectedCurrency = state.currency,
                    isLoading = false,
                    amount = state.amount,
                    date = parseDateString(state.date),
                    notes = state.notes,
                    isSaveEnabled = state.isEditing && state.title.isNotEmpty() && state.category.isNotEmpty() && state.amount.isNotEmpty(),
                    isReadOnly = !state.isEditing,
                    showTypeToggle = state.isEditing
                )

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
                transactionId = "",
                isIncome = false,
                title = "Grocery Shopping",
                category = "Food",
                amount = "85.50",
                currency = "USD",
                date = "2024-03-15".toLong(),
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
                transactionId = "",
                isIncome = true,
                title = "Freelance Work",
                category = "Income",
                amount = "800.00",
                currency = "USD",
                date = "2024-03-12".toLong(),
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
