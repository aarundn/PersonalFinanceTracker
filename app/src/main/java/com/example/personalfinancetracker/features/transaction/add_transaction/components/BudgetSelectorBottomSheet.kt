package com.example.personalfinancetracker.features.transaction.add_transaction.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.core.components.BudgetAmountStateInfo
import com.example.core.components.BudgetInfo
import com.example.core.ui.theme.ProgressError
import com.example.core.ui.theme.Warning
import com.example.personalfinancetracker.features.budget.model.BudgetUi
import com.example.personalfinancetracker.features.budget.utils.formatCurrency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetSelectorBottomSheet(
    budgets: List<BudgetUi>,
    selectedBudgetId: String?,
    onBudgetSelected: (String) -> Unit,
    onAddBudgetClicked: () -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "Select Budget",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                // Spacer to balance the close button
                Spacer(modifier = Modifier.size(48.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (budgets.isEmpty()) {
                Text(
                    text = "No budgets found for this category.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(budgets, key = { it.id }) { budget ->
                        BudgetSelectorCard(
                            budget = budget,
                            isSelected = budget.id == selectedBudgetId,
                            onClick = {
                                onBudgetSelected(budget.id)
                                onDismiss()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add Budget button at the bottom
            TextButton(
                onClick = onAddBudgetClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Create New Budget")
            }
        }
    }
}

@Composable
private fun BudgetSelectorCard(
    budget: BudgetUi,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconTint = budget.currentCategory.color
    val iconBackground = iconTint.copy(alpha = 0.12f)

    val progressColor = when {
        budget.isOverBudget -> ProgressError
        budget.isWarning -> Warning
        else -> iconTint
    }

    val borderColor = if (isSelected) iconTint else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    val borderWidth = if (isSelected) 2.dp else 1.dp

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = BorderStroke(borderWidth, borderColor),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BudgetInfo(
                modifier = Modifier.weight(1f),
                iconTint = iconTint,
                iconBackground = iconBackground,
                icon = budget.currentCategory.icon,
                categoryName = budget.currentCategory.nameResId,
                currencySymbol = budget.currencySymbol,
                spent = budget.spent.formatCurrency(),
                amount = budget.amount.formatCurrency()
            )
            BudgetAmountStateInfo(
                isOverBudget = budget.isOverBudget,
                isWarning = budget.isWarning,
                progressColor = progressColor,
                overBudget = budget.overBudget.formatCurrency(),
                remaining = budget.remaining.formatCurrency(),
                currencySymbol = budget.currencySymbol
            )
        }
    }
}
