package com.example.personalfinancetracker.features.budget.edit_budget

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.components.ConfirmationDialog
import com.example.core.components.HeaderSection
import com.example.core.components.LoadingIndicator
import com.example.core.model.DefaultCategories
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.ui.theme.ProgressError
import com.example.personalfinancetracker.features.budget.common.BudgetInputForm
import com.example.personalfinancetracker.features.budget.common.BudgetPeriodOptions
import com.example.personalfinancetracker.features.budget.edit_budget.components.BudgetInsightsCard
import com.example.personalfinancetracker.features.budget.edit_budget.components.BudgetOverviewCard

@Composable
fun EditBudgetScreen(
    state: EditBudgetState,
    onEvent: (EditBudgetEvent) -> Unit,
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState,
) {

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackBarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            HeaderSection(
                onBackClick = { onEvent(EditBudgetEvent.OnCancel) },
                title = if (state.isEditing) "Edit Budget" else "Budget Details",
                actions = {
                    if (!state.isEditing) {
                        IconButton(onClick = { onEvent(EditBudgetEvent.OnEdit) }) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "Edit budget"
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
            if (state.isLoading && !state.isEditing) {
                LoadingIndicator(modifier = Modifier.padding(vertical = 32.dp))
            } else {
                state.budget?.let { budget ->
                    BudgetOverviewCard(
                        budget = budget,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                BudgetInputForm(
                    categories = state.categories,
                    selectedCategory = state.selectedCategory,
                    selectedPeriodId = state.periodInput,
                    amount = state.amountInput,
                    notes = state.notesInput,
                    selectedCurrency = state.selectedCurrency,
                    onCategorySelected = { onEvent(EditBudgetEvent.OnCategoryChanged(it)) },
                    onPeriodSelected = { onEvent(EditBudgetEvent.OnPeriodChanged(it)) },
                    onAmountChanged = { onEvent(EditBudgetEvent.OnAmountChanged(it)) },
                    onCurrencySelected = { onEvent(EditBudgetEvent.OnCurrencyChanged(it)) },
                    onNotesChanged = { onEvent(EditBudgetEvent.OnNotesChanged(it)) },
                    onSave = { onEvent(EditBudgetEvent.OnSave) },
                    onCancel = { onEvent(EditBudgetEvent.OnCancel) }, // Note: OnCancelEdit was removed/mapped to OnCancel or we need to check logic
                    isLoading = state.isLoading,
                    isReadOnly = !state.isEditing
                )

                BudgetInsightsCard(state = state)

                if (!state.isEditing) {
                    DangerZoneCard(
                        onDelete = { onEvent(EditBudgetEvent.OnDelete) }
                    )
                }
            }
        }
    }

    if (state.showDeleteConfirmation) {
        ConfirmationDialog(
            title = "Delete Budget",
            message = "Are you sure you want to delete this budget? This action cannot be undone.",
            confirmText = "Delete",
            dismissText = "Cancel",
            onConfirm = { onEvent(EditBudgetEvent.OnConfirmDelete) },
            onDismiss = { onEvent(EditBudgetEvent.OnDismissDelete) }
        )
    }
}

@Composable
private fun DangerZoneCard(
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = BorderStroke(1.dp, ProgressError.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Danger Zone",
                style = MaterialTheme.typography.titleMedium,
                color = ProgressError,
                fontWeight = FontWeight.SemiBold
            )
            Button(
                onClick = onDelete,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ProgressError,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Delete Budget")
            }
            Text(
                text = "This action cannot be undone. This will permanently delete your budget.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditBudgetScreenPreview() {
    PersonalFinanceTrackerTheme {
        EditBudgetScreen(
            state = EditBudgetState(
                selectedCategory = DefaultCategories.FOOD,
                budget = com.example.personalfinancetracker.features.budget.model.BudgetUi(
                    id = "1",
                    userId = "user",
                    category = DefaultCategories.FOOD.id,
                    amount = 500.0,
                    spent = 420.0,
                    currency = "USD",
                    period = BudgetPeriodOptions.Monthly.id,
                    notes = "Groceries",
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                ),
                amountInput = "500.00",
                periodInput = BudgetPeriodOptions.Monthly.id,
                notesInput = "Groceries and dining out",
                categories = DefaultCategories.getCategories(isIncome = false)
            ),
            onEvent = {},
            snackBarHostState = SnackbarHostState()
        )
    }
}

