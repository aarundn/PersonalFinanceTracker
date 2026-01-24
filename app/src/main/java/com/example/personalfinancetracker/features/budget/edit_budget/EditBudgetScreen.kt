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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.components.FormInput
import com.example.core.components.LoadingIndicator
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.ui.theme.ProgressError
import com.example.personalfinancetracker.features.budget.edit_budget.components.ActionButtons
import com.example.personalfinancetracker.features.budget.edit_budget.components.BudgetInsightsCard
import com.example.personalfinancetracker.features.budget.edit_budget.components.BudgetOverviewCard
import com.example.personalfinancetracker.features.budget.edit_budget.components.BudgetPeriodSelector
import com.example.personalfinancetracker.features.budget.edit_budget.components.HeaderSection

@Composable
fun EditBudgetScreen(
    state: EditBudgetContract.State,
    onEvent: (EditBudgetContract.Event) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        onEvent(EditBudgetContract.Event.OnLoadBudget)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            HeaderSection(
                onBackClick = { onEvent(EditBudgetContract.Event.OnCancel) },
                title = if (state.isEditing) "Edit Budget" else "Budget Details",
                actions = {
                    if (!state.isEditing) {
                        IconButton(onClick = { onEvent(EditBudgetContract.Event.OnEdit) }) {
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
                BudgetOverviewCard(
                    state = state,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        if (state.isEditing) {
                            Text(
                                text = "Budget Information",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            FormInput(
                                label = "Budget Amount",
                                value = state.budgetedAmountInput,
                                onValueChange = { onEvent(EditBudgetContract.Event.OnBudgetAmountChanged(it)) },
                                placeholder = "Enter budget amount",
                                keyboardType = KeyboardType.Decimal
                            )

                            BudgetPeriodSelector(
                                selectedPeriodId = state.periodInput,
                                onPeriodSelected = { onEvent(EditBudgetContract.Event.OnPeriodChanged(it)) }
                            )

                            FormInput(
                                label = "Notes",
                                minLine = 3,
                                maxLine = 5,
                                value = state.notesInput,
                                onValueChange = { onEvent(EditBudgetContract.Event.OnNotesChanged(it)) },
                                placeholder = "Add notes about this budget..."
                            )

                            ActionButtons(
                                onCancel = { onEvent(EditBudgetContract.Event.OnCancelEdit) },
                                onSave = { onEvent(EditBudgetContract.Event.OnSave) },
                                isLoading = state.isLoading,
                                isSaveEnabled = state.budgetedAmountInput.isNotBlank()
                            )
                        } else {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    text = "Budget Information",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                InfoRow(
                                    label = "Budget Amount",
                                    value = "$${state.budgetedAmount.formatCurrency()}"
                                )

                                HorizontalDivider()

                                InfoRow(
                                    label = "Period",
                                    value = EditBudgetContract.PeriodOptions.findById(state.periodInput).label
                                )

                                HorizontalDivider()

                                InfoRow(
                                    label = "Daily Average",
                                    value = "$${state.averageDailySpend.formatCurrency()}"
                                )

                                if (state.notesOriginal.isNotEmpty()) {
                                    HorizontalDivider()
                                    InfoRow(
                                        label = "Notes",
                                        value = state.notesOriginal,
                                        muted = true
                                    )
                                }
                            }
                        }
                    }
                }

                BudgetInsightsCard(state = state)

                if (!state.isEditing) {
                    DangerZoneCard(
                        onDelete = { onEvent(EditBudgetContract.Event.OnDelete) }
                    )
                }

                state.error?.let { errorMessage ->
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }
            }
        }
    }

    if (state.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { onEvent(EditBudgetContract.Event.OnDismissDeleteDialog) },
            title = { Text("Delete Budget") },
            text = { Text("This action cannot be undone. Do you still want to delete this budget?") },
            confirmButton = {
                Button(
                    onClick = { onEvent(EditBudgetContract.Event.OnConfirmDelete) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ProgressError,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(
                    onClick = { onEvent(EditBudgetContract.Event.OnDismissDeleteDialog) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    muted: Boolean = false
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = if (muted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
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

private fun Double.formatCurrency(): String = String.format("%,.2f", this)

@Preview(showBackground = true)
@Composable
private fun EditBudgetScreenPreview() {
    PersonalFinanceTrackerTheme {
        EditBudgetScreen(
            state = EditBudgetContract.State(
                budgetId = 1,
                category = "Food & Dining",
                budgetedAmountOriginal = 500.0,
                budgetedAmountInput = "500.00",
                spentAmount = 420.0,
                periodInput = EditBudgetContract.PeriodOptions.Monthly.id,
                periodOriginal = EditBudgetContract.PeriodOptions.Monthly.id,
                notesOriginal = "Groceries and dining out",
                notesInput = "Groceries and dining out"
            ),
            onEvent = {}
        )
    }
}
