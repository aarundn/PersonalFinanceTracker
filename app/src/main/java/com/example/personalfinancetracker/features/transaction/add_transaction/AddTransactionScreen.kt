package com.example.personalfinancetracker.features.transaction.add_transaction

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.components.TransactionInputForm
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.personalfinancetracker.features.transaction.add_transaction.components.HeaderSection

@Composable
fun AddTransactionScreen(
    state: AddTransactionContract.State,
    onEvent: (AddTransactionContract.Event) -> Unit,
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
        TransactionInputForm(
            modifier = Modifier.padding(innerPadding),
            isIncome = state.isIncome,
            onTypeChanged = { onEvent(AddTransactionContract.Event.OnTransactionTypeChanged(it)) },
            selectedCategoryName = state.category,
            onCategorySelected = { onEvent(AddTransactionContract.Event.OnCategoryChanged(it)) },
            onDateChanged = { onEvent(AddTransactionContract.Event.OnDateChanged(it))},
            onAmountChanged = { onEvent(AddTransactionContract.Event.OnAmountChanged(it)) },
            onNotesChanged = { onEvent(AddTransactionContract.Event.OnNotesChanged(it)) },
            onCurrencySelected = { onEvent(AddTransactionContract.Event.OnCurrencyChanged(it)) },
            onSave = { onEvent(AddTransactionContract.Event.OnSave) },
            onCancel = { onEvent(AddTransactionContract.Event.OnCancel) },
            currency = state.currency,
            isLoading = state.isLoading,
            amount = state.amount,
            date = state.date,
            notes = state.notes,
            isSaveEnabled = state.category.isNotEmpty() && state.amount.isNotEmpty()
        )
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
                date = 0L,
                notes = "Lunch at restaurant"
            ),
            onEvent = {}
        )
    }
}