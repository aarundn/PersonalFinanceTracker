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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.R
import com.example.core.components.ConfirmationDialog
import com.example.core.components.CustomSnackBar
import com.example.core.components.HeaderSection
import com.example.core.components.LoadingIndicator
import com.example.core.components.TransactionInputForm
import com.example.core.model.DefaultCategories
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.model.DefaultCurrencies
import com.example.core.ui.theme.ProgressError
import com.example.core.utils.parseDateString
import com.example.personalfinancetracker.features.transaction.edit_transaction.components.TransactionOverviewCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen(
    state: EditTransactionState,
    onEvent: (EditTransactionEvent) -> Unit,
    snackBarHostState: SnackbarHostState,
) {


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(snackBarHostState, Modifier) { snackBarData ->
                CustomSnackBar(snackBarData, modifier = Modifier.padding(16.dp))
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            HeaderSection(
                onBackClick = { onEvent(EditTransactionEvent.OnCancel) },
                title = if (state.isEditing) "Edit Transaction" else "Transaction Details",
                actions = {
                    if (!state.isEditing) {
                        IconButton(onClick = { onEvent(EditTransactionEvent.OnEdit) }) {
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
                state.transaction?.let {
                    TransactionOverviewCard(
                        transactionUi = it,
                        modifier = Modifier.padding(top = 16.dp),
                    )
                }

                TransactionInputForm(
                    modifier = Modifier,
                    isIncome = state.isIncome,
                    onTypeChanged = { if (state.isEditing)
                        onEvent(EditTransactionEvent.OnTransactionTypeChanged(it)) },
                    selectedCategory = state.selectedCategory,
                    onCategorySelected = { if (state.isEditing)
                        onEvent(EditTransactionEvent.OnCategoryChanged(it)) },
                    onDateChanged = { if (state.isEditing)
                        onEvent(EditTransactionEvent.OnDateChanged(it.toLong())) },
                    onAmountChanged = { if (state.isEditing)
                        onEvent(EditTransactionEvent.OnAmountChanged(it)) },
                    onNotesChanged = { if (state.isEditing)
                        onEvent(EditTransactionEvent.OnNotesChanged(it)) },
                    onCurrencySelected = { if (state.isEditing)
                        onEvent(EditTransactionEvent.OnCurrencyChanged(it)) },
                    onSave = { onEvent(EditTransactionEvent.OnSave) },
                    onCancel = { onEvent(EditTransactionEvent.OnCancel) },
                    selectedCurrency = state.selectedCurrency,
                    isLoading = false,
                    amount = state.amount,
                    date = parseDateString(state.date),
                    notes = state.notes,
                    isReadOnly = !state.isEditing,
                    showTypeToggle = state.isEditing,
                    categories = state.categories
                )

                if (!state.isEditing) {
                    Button(
                        onClick = { onEvent(EditTransactionEvent.OnDelete) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ProgressError,
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

        if (state.showDeleteConfirmation) {
            ConfirmationDialog(
                title = "Delete Transaction",
                message = "Are you sure you want to delete this transaction? This action cannot be undone.",
                confirmText = "Delete",
                dismissText = "Cancel",
                icon = ImageVector.vectorResource(id = R.drawable.shopping_basket),
                onConfirm = { onEvent(EditTransactionEvent.OnConfirmDelete) },
                onDismiss = { onEvent(EditTransactionEvent.OnDismissDelete) }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun EditTransactionScreenPreview() {
    PersonalFinanceTrackerTheme {
        EditTransactionScreen(
            state = EditTransactionState(
                isIncome = false,
                selectedCategory = DefaultCategories.FOOD,
                amount = "85.50",
                selectedCurrency = DefaultCurrencies.USD,
                date = "2024-03-15".toLong(),
                notes = "Weekly grocery shopping at Whole Foods",
                isEditing = false,
            ),
            onEvent = {},
            snackBarHostState = SnackbarHostState()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditTransactionScreenEditingPreview() {
    PersonalFinanceTrackerTheme {
        EditTransactionScreen(
            state = EditTransactionState(
                isIncome = true,
                selectedCategory = DefaultCategories.SALARY,
                amount = "800.00",
                selectedCurrency = DefaultCurrencies.USD,
                date = "2024-03-12".toLong(),
                notes = "Website development project",
                isEditing = true,
            ),
            onEvent = {},
            snackBarHostState = SnackbarHostState()

        )
    }
}
