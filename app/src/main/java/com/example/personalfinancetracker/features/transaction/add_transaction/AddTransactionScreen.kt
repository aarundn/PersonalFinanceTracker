package com.example.personalfinancetracker.features.transaction.add_transaction

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.components.TransactionInputForm
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.components.HeaderSection
import com.example.core.utils.parseDateString
import com.example.personalfinancetracker.features.transaction.add_transaction.TransactionEvent.OnCancel
import com.example.personalfinancetracker.features.transaction.add_transaction.TransactionEvent.OnSave
import com.example.personalfinancetracker.features.transaction.add_transaction.TransactionEvent.OnDateChanged
import com.example.personalfinancetracker.features.transaction.add_transaction.TransactionEvent.OnAmountChanged
import com.example.personalfinancetracker.features.transaction.add_transaction.TransactionEvent.OnNotesChanged
import com.example.personalfinancetracker.features.transaction.add_transaction.TransactionEvent.OnCurrencyChanged
import com.example.personalfinancetracker.features.transaction.add_transaction.TransactionEvent.OnCategoryChanged
import com.example.personalfinancetracker.features.transaction.add_transaction.TransactionEvent.OnTransactionTypeChanged

@Composable
fun AddTransactionScreen(
    snackBarHostState: SnackbarHostState,
    state: TransactionState,
    onEvent: (TransactionEvent) -> Unit,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { HeaderSection(onBackClick = { onEvent(OnCancel) }) }
    ) { innerPadding ->

        TransactionInputForm(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            categories = state.categories,
            isIncome = state.isIncome,
            onTypeChanged = { onEvent(OnTransactionTypeChanged(it)) },
            selectedCategoryName = state.category,
            onCategorySelected = { onEvent(OnCategoryChanged(it)) },
            onDateChanged = { onEvent(OnDateChanged(it.toLongOrNull() ?: 0L)) },
            onAmountChanged = { onEvent(OnAmountChanged(it)) },
            onNotesChanged = { onEvent(OnNotesChanged(it)) },
            onCurrencySelected = { onEvent(OnCurrencyChanged(it)) },
            onSave = { onEvent(OnSave) },
            onCancel = { onEvent(OnCancel) },
            selectedCurrency = state.currency,
            isLoading = state.isLoading,
            amount = state.amount,
            date = parseDateString(state.date),
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
            state = TransactionState(
                isIncome = false,
                title = "Sample Transaction",
                category = "Food",
                amount = "25.50",
                currency = "USD",
                date = 0L,
                notes = "Lunch at restaurant"
            ),
            onEvent = {},
            snackBarHostState = SnackbarHostState()
        )
    }
}