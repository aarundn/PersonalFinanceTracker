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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.components.CustomProgressBar
import com.example.core.model.DefaultCategories
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.ui.theme.ProgressError
import com.example.core.ui.theme.Warning
import com.example.personalfinancetracker.features.budget.common.BudgetPeriodOptions
import com.example.personalfinancetracker.features.budget.edit_budget.EditBudgetContract

@Composable
fun BudgetOverviewCard(
    state: EditBudgetContract.State,
    modifier: Modifier = Modifier
) {
    val category = state.selectedCategory
    val tint = category?.color ?: MaterialTheme.colorScheme.primary
    val containerColor = tint.copy(alpha = 0.12f)
    val icon = category?.let { ImageVector.vectorResource(it.icon) } ?: Icons.Outlined.Home
    val percentageUsed = state.percentageUsed.coerceAtLeast(0f)
    val progressColor = when {
        state.isOverBudget -> ProgressError
        state.isWarning -> Warning
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
                    androidx.compose.material3.Icon(
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
                        text = state.selectedCategory?.let { stringResource(it.nameResId) } ?: "Budget Category",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${periodLabel(state.periodInput)} Budget",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                StatusBadge(
                    isOverBudget = state.isOverBudget,
                    isWarning = state.isWarning
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AmountRow(
                    label = "Spent",
                    value = state.spentAmount,
                    emphasize = true
                )
                AmountRow(
                    label = "Budget",
                    value = state.budgetedAmount
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
                        text = "${(percentageUsed * 100).coerceAtMost(999f).formatPercent()} used",
                        style = MaterialTheme.typography.bodySmall,
                        color = progressColor
                    )
                    val remaining = state.remainingAmount
                    Text(
                        text = if (remaining >= 0) {
                            "$${remaining.formatCurrency()} left"
                        } else {
                            "$${kotlin.math.abs(remaining).formatCurrency()} over"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = if (remaining >= 0) MaterialTheme.colorScheme.primary else ProgressError,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(
    isOverBudget: Boolean,
    isWarning: Boolean,
    modifier: Modifier = Modifier
) {
    when {
        isOverBudget -> {
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(12.dp),
                color = ProgressError.copy(alpha = 0.12f)
            ) {
                Text(
                    text = "Over Budget",
                    style = MaterialTheme.typography.labelMedium,
                    color = ProgressError,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        isWarning -> {
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(12.dp),
                color = Warning.copy(alpha = 0.12f)
            ) {
                Text(
                    text = "Almost Full",
                    style = MaterialTheme.typography.labelMedium,
                    color = Warning,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun AmountRow(
    label: String,
    value: Double,
    emphasize: Boolean = false
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
            text = "$${value.formatCurrency()}",
            style = if (emphasize) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge,
            fontWeight = if (emphasize) FontWeight.SemiBold else FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun Double.formatCurrency(): String = String.format("%,.2f", this)

private fun Float.formatPercent(): String = String.format("%.1f%%", this)

private fun periodLabel(id: String): String {
    return BudgetPeriodOptions.findById(id).label
}

@Preview(showBackground = true)
@Composable
private fun BudgetOverviewCardPreview() {
    PersonalFinanceTrackerTheme {
        BudgetOverviewCard(
            state = EditBudgetContract.State(
                selectedCategory = DefaultCategories.FOOD,
                budgetedAmountOriginal = 500.0,
                budgetedAmountInput = "500.00",
                spentAmount = 420.0,
                periodInput = BudgetPeriodOptions.Monthly.id,
                periodOriginal = BudgetPeriodOptions.Monthly.id
            )
        )
    }
}

