package com.example.personalfinancetracker.features.budget.add_budget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.components.FormInput
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.personalfinancetracker.features.budget.add_budget.components.ActionButtons
import com.example.personalfinancetracker.features.budget.add_budget.components.BudgetPreviewCard
import com.example.personalfinancetracker.features.budget.add_budget.components.CategorySelectionGrid
import com.example.core.components.HeaderSection
import com.example.personalfinancetracker.features.budget.edit_budget.components.BudgetPeriodSelector

@Composable
fun AddBudgetScreen(
    state: AddBudgetContract.State,
    onEvent: (AddBudgetContract.Event) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        onEvent(AddBudgetContract.Event.OnLoad)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            HeaderSection(
                onBackClick = { onEvent(AddBudgetContract.Event.OnCancel) },
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
            Card(
                modifier = Modifier.padding(top = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "Budget Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    CategorySelectionGrid(
                        categories = state.categories,
                        selectedCategoryId = state.categoryId,
                        onCategorySelected = { onEvent(AddBudgetContract.Event.OnCategorySelected(it)) }
                    )

                    FormInput(
                        label = "Budget Amount (USD)",
                        value = state.amountInput,
                        onValueChange = { onEvent(AddBudgetContract.Event.OnAmountChanged(it)) },
                        placeholder = "Enter budget amount"
                    )

                    BudgetPeriodSelector(
                        selectedPeriodId = state.periodId,
                        onPeriodSelected = { onEvent(AddBudgetContract.Event.OnPeriodChanged(it)) }
                    )

                    if (state.categoryId.isNotEmpty() && state.amount > 0.0) {
                        state.selectedCategory?.let { category ->
                            BudgetPreviewCard(
                                category = category,
                                amount = state.amount,
                                dailyAverage = state.dailyAverage,
                                periodLabel = state.period.label
                            )
                        }
                    }

                    FormInput(
                        label = "Notes (Optional)",
                        value = state.notes,
                        maxLine = 5,
                        minLine = 3,
                        onValueChange = { onEvent(AddBudgetContract.Event.OnNotesChanged(it)) },
                        placeholder = "Add any notes about this budget..."
                    )
                }
            }

            ActionButtons(
                onCancel = { onEvent(AddBudgetContract.Event.OnCancel) },
                onSave = { onEvent(AddBudgetContract.Event.OnSave) },
                isSaving = state.isSaving,
                isSaveEnabled = state.isSaveEnabled
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
            state = AddBudgetContract.State(
                categories = listOf(
                    AddBudgetContract.Category(
                        id = "food",
                        name = "Food & Dining",
                        icon = Icons.Outlined.Home,
                        iconTint = Color(0xFFF97316),
                        iconBackground = Color(0xFFFFEDD5)
                    )
                )
            ),
            onEvent = {}
        )
    }
}
