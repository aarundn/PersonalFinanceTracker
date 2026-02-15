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
import com.example.core.model.DefaultCurrencies
import com.example.core.utils.parseDateString

@Composable
fun AddTransactionScreen(
    snackBarHostState: SnackbarHostState,
    state: AddTransactionState,
    onEvent: (AddTransactionEvent) -> Unit,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { HeaderSection(onBackClick = { onEvent(AddTransactionEvent.OnCancel) }) }
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
            onTypeChanged = { onEvent(AddTransactionEvent.OnTransactionTypeChanged(it)) },
            selectedCategory = state.selectedCategory,
            onCategorySelected = { onEvent(AddTransactionEvent.OnCategoryChanged(it)) },
            onDateChanged = { onEvent(AddTransactionEvent.OnDateChanged(it.toLongOrNull() ?: 0L)) },
            onAmountChanged = { onEvent(AddTransactionEvent.OnAmountChanged(it)) },
            onNotesChanged = { onEvent(AddTransactionEvent.OnNotesChanged(it)) },
            onCurrencySelected = { onEvent(AddTransactionEvent.OnCurrencyChanged(it)) },
            onSave = { onEvent(AddTransactionEvent.OnSave) },
            onCancel = { onEvent(AddTransactionEvent.OnCancel) },
            selectedCurrency = state.selectedCurrency,
            isLoading = state.isLoading,
            amount = state.amount,
            date = parseDateString(state.date),
            notes = state.notes,
            isSaveEnabled = state.selectedCategory != null && state.amount.isNotEmpty()
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun AddTransactionScreenPreview() {
    PersonalFinanceTrackerTheme {
        AddTransactionScreen(
            state = AddTransactionState(
                isIncome = false,
                amount = "25.50",
                selectedCurrency = DefaultCurrencies.USD,
                date = 0L,
                notes = "Lunch at restaurant",
                selectedCategory = null
            ),
            onEvent = {},
            snackBarHostState = SnackbarHostState()
        )
    }
}
