package com.example.personalfinancetracker.features.budget.budgets.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.components.BudgetAmountStateInfo
import com.example.core.components.BudgetInfo
import com.example.core.components.BudgetStatusBadge
import com.example.core.components.CustomProgressBar
import com.example.core.ui.theme.Income
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.ui.theme.ProgressError
import com.example.core.ui.theme.Warning
import com.example.core.ui.theme.dimensions
import com.example.core.utils.formatAmountNoSpace
import com.example.core.utils.formatPercentage
import com.example.data.sync.SyncStatusEnum
import com.example.personalfinancetracker.features.budget.model.BudgetUi
import com.example.personalfinancetracker.features.budget.utils.formatCurrency

@Composable
fun BudgetCard(
    budget: BudgetUi,
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

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(MaterialTheme.dimensions.radiusMedium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = BorderStroke(MaterialTheme.dimensions.borderThin, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
    ) {
        Column(
            modifier = Modifier.padding(MaterialTheme.dimensions.spacingMedium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.spacingMediumSmall)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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

            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.spacingExtraSmall)
            ) {
                CustomProgressBar(
                    progress = budget.percentage,
                    modifier = Modifier.fillMaxWidth(),
                    progressColor = progressColor,
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatPercentage(budget.percentage),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = formatAmountNoSpace(budget.currencySymbol, budget.amount.formatCurrency()),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            BudgetStatusBadge(
                text = budget.syncStatusEnum,
                color = when (budget.syncStatusEnum) {
                    SyncStatusEnum.PENDING.name -> ProgressError
                    SyncStatusEnum.SYNCED.name -> Income
                    SyncStatusEnum.SYNCING.name -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.outline
                },
                modifier = Modifier.align(Alignment.End),
                icon = null
            )
        }
    }
}

@Preview
@Composable
private fun BudgetCardPreview() {
    PersonalFinanceTrackerTheme {
        BudgetCard(
            budget = BudgetUi(
                id = "1",
                category = "food",
                amount = 500.0,
                spent = 550.0,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                period = "monthly",
                notes = "",
                currency = "USD",
                userId = "",
                syncStatusEnum = SyncStatusEnum.PENDING.name
            ),
            onClick = {}
        )
    }

}
