package com.example.personalfinancetracker.features.budget.edit_budget.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.components.BudgetStatusBadge
import com.example.core.components.CustomProgressBar
import com.example.core.model.DefaultCategories
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.ui.theme.ProgressError
import com.example.core.ui.theme.Warning
import com.example.domain.model.BudgetPeriod
import com.example.personalfinancetracker.features.budget.model.BudgetUi
import com.example.personalfinancetracker.features.budget.utils.formatCurrency

@Composable
fun BudgetOverviewCard(
    budget: BudgetUi,
    modifier: Modifier = Modifier,
) {
    val category = budget.currentCategory
    val tint = category.color
    val containerColor = tint.copy(alpha = 0.12f)
    val icon = category.let { ImageVector.vectorResource(it.icon) }
    val percentageUsed = budget.percentage.coerceAtLeast(0f)

    val isOverBudget = budget.isOverBudget
    val isWarning = budget.isWarning

    val progressColor = when {
        isOverBudget -> ProgressError
        isWarning -> Warning
        else -> MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .background(color = containerColor, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = tint,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(category.nameResId),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${budget.period} Budget",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                when {
                    isOverBudget -> BudgetStatusBadge(
                        text = "Over Budget",
                        color = ProgressError
                    )

                    isWarning -> BudgetStatusBadge(
                        text = "Almost Full",
                        color = Warning
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AmountRow(
                    label = "Spent",
                    value = budget.spent,
                    emphasize = true,
                    currencySymbol = budget.currencySymbol
                )
                AmountRow(
                    label = "Budget",
                    value = budget.amount,
                    currencySymbol = budget.currencySymbol
                )

                CustomProgressBar(
                    progress = percentageUsed.coerceIn(0f, 1f),
                    modifier = Modifier.fillMaxWidth(),
                    progressColor = progressColor,
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${(percentageUsed * 100).coerceAtMost(999f)
                            .toDouble().formatCurrency()} used",
                        style = MaterialTheme.typography.bodySmall,
                        color = progressColor
                    )
                    val remaining = budget.remaining
                    Text(
                        text = if (remaining >= 0) {
                            "${budget.currencySymbol} ${remaining.formatCurrency()} left"
                        } else {
                            "${budget.currencySymbol} ${budget.overBudget.formatCurrency()} over"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = if (remaining >= 0) progressColor else ProgressError,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun AmountRow(
    label: String,
    value: Double,
    emphasize: Boolean = false,
    currencySymbol: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "$currencySymbol ${value.formatCurrency()}",
            style = if (emphasize) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge,
            fontWeight = if (emphasize) FontWeight.SemiBold else FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}




@Preview(showBackground = true)
@Composable
private fun BudgetOverviewCardPreview() {
    PersonalFinanceTrackerTheme {
        BudgetOverviewCard(

            budget = BudgetUi(
                id = "1",
                userId = "A1",
                category = DefaultCategories.FOOD.id,
                amount = 500.0,
                currency = "USD",
                period = BudgetPeriod.Monthly.id,
                notes = null,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                spent = 420.0
            ),
        )
    }
}
