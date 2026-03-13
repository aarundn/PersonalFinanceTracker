package com.example.personalfinancetracker.features.transaction.add_transaction

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.components.CustomDatePickerDialog
import com.example.core.components.CustomSnackBar
import com.example.core.components.HeaderSection
import com.example.core.components.TransactionInputForm
import com.example.core.model.DefaultCurrencies
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.utils.parseDateString
import com.example.personalfinancetracker.features.budget.mapper.toDisplayData
import com.example.personalfinancetracker.features.transaction.add_transaction.components.BudgetSelectorBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    snackBarHostState: SnackbarHostState,
    state: AddTransactionState,
    onEvent: (AddTransactionEvent) -> Unit,
) {
    val budgetSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState, Modifier) { snackBarData ->
            CustomSnackBar(snackBarData, modifier = Modifier.padding(16.dp))
        } },
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
            selectedBudget = state.selectedBudget?.toDisplayData(),
            onLinkBudgetClicked = { onEvent(AddTransactionEvent.OnShowBudgetSelector) },
            onAmountChanged = { onEvent(AddTransactionEvent.OnAmountChanged(it)) },
            onNotesChanged = { onEvent(AddTransactionEvent.OnNotesChanged(it)) },
            onCurrencySelected = { onEvent(AddTransactionEvent.OnCurrencyChanged(it)) },
            onSave = { onEvent(AddTransactionEvent.OnSave) },
            onCancel = { onEvent(AddTransactionEvent.OnCancel) },
            selectedCurrency = state.selectedCurrency,
            isLoading = state.isLoading,
            amount = state.amount,
            date = parseDateString(state.date),
            onDatePickerClicked = { onEvent(AddTransactionEvent.OnShowDatePicker) },
            notes = state.notes,
        )
    }

    if (state.showBudgetSelector) {
        BudgetSelectorBottomSheet(
            budgets = state.availableBudgets,
            selectedBudgetId = state.selectedBudget?.id,
            onBudgetSelected = { onEvent(AddTransactionEvent.OnBudgetSelected(it)) },
            onAddBudgetClicked = { onEvent(AddTransactionEvent.OnAddBudgetClicked) },
            onDismiss = { onEvent(AddTransactionEvent.OnHideBudgetSelector) },
            sheetState = budgetSheetState
        )
    }

    if (state.showDatePicker) {
        CustomDatePickerDialog(
            initialSelectedDateMillis = state.date,
            onDateSelected = {
                onEvent(AddTransactionEvent.OnDateChanged(it))
            },
            onDismiss = { onEvent(AddTransactionEvent.OnHideDatePicker) }
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
            snackBarHostState = SnackbarHostState(),
        )
    }
}
