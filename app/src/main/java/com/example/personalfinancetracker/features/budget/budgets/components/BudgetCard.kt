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
import androidx.compose.ui.unit.dp
import com.example.core.components.BudgetAmountStateInfo
import com.example.core.components.BudgetInfo
import com.example.core.components.CustomProgressBar
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.ui.theme.ProgressError
import com.example.core.ui.theme.Warning
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
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
                verticalArrangement = Arrangement.spacedBy(4.dp)
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
                        text = "${(budget.percentage * 100).toDouble().formatCurrency()}%",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${budget.currencySymbol}${budget.amount.formatCurrency()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
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
                userId = ""
            ),
            onClick = {}
        )
    }

}
