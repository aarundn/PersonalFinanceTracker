package com.example.personalfinancetracker.features.budget.add_budget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.model.DefaultCategories
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.components.HeaderSection
import com.example.personalfinancetracker.features.budget.common.BudgetInputForm

@Composable
fun AddBudgetScreen(
    snackBarHostState: SnackbarHostState,
    state: AddBudgetState,
    onEvent: (AddBudgetEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            HeaderSection(
                onBackClick = { onEvent(AddBudgetEvent.OnCancel) },
                title = "Add Budget"
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
            BudgetInputForm(
                modifier = Modifier.padding(top = 16.dp),
                categories = state.categories,
                selectedCategory = state.selectedCategory,
                selectedPeriodId = state.periodId,
                amount = state.amountInput,
                notes = state.notes,
                selectedCurrency = state.selectedCurrency,
                onCategorySelected = { onEvent(AddBudgetEvent.OnCategorySelected(it)) },
                onPeriodSelected = { onEvent(AddBudgetEvent.OnPeriodChanged(it)) },
                onAmountChanged = { onEvent(AddBudgetEvent.OnAmountChanged(it)) },
                onCurrencySelected = { onEvent(AddBudgetEvent.OnCurrencyChanged(it)) },
                onNotesChanged = { onEvent(AddBudgetEvent.OnNotesChanged(it)) },
                onSave = { onEvent(AddBudgetEvent.OnSave) },
                onCancel = { onEvent(AddBudgetEvent.OnCancel) },
                isLoading = state.isSaving,
            )

            state.error?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddBudgetScreenPreview() {
    PersonalFinanceTrackerTheme {
        AddBudgetScreen(
            state = AddBudgetState(
                categories = DefaultCategories.getCategories(isIncome = false),
                selectedCategory = DefaultCategories.FOOD
            ),
            onEvent = {},
            snackBarHostState = SnackbarHostState()
        )
    }
}
