package com.example.personalfinancetracker.features.transaction.add_transaction.components

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.core.ui.theme.AppTheme
import com.example.core.components.BudgetSelectorCard
import com.example.personalfinancetracker.R
import com.example.personalfinancetracker.features.budget.mapper.toDisplayData
import com.example.personalfinancetracker.features.budget.model.BudgetUi

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
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = AppTheme.dimensions.spacingMedium)
        ) {
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
                Spacer(modifier = Modifier.size(AppTheme.dimensions.buttonHeightNormal))
            }

            Spacer(modifier = Modifier.height(AppTheme.dimensions.spacingMedium))

            if (budgets.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_budgets_found),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = AppTheme.dimensions.spacingExtraLarge),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacingMediumSmall),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(budgets, key = { it.id }) { budget ->
                        BudgetSelectorCard(
                            budget = budget.toDisplayData(),
                            isSelected = budget.id == selectedBudgetId,
                            onClick = {
                                onBudgetSelected(budget.id)
                                onDismiss()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(AppTheme.dimensions.spacingMedium))
            TextButton(
                onClick = onAddBudgetClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.dimensions.spacingMedium)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.padding(end = AppTheme.dimensions.spacingSmall)
                )
                Text("Create New Budget")
            }
        }
    }
}
