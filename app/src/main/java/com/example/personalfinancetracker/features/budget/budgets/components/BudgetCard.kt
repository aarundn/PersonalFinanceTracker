package com.example.personalfinancetracker.features.budget.budgets.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.personalfinancetracker.features.budget.model.BudgetUi

@Composable
fun BudgetCard(
    budget: BudgetUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val category = DefaultCategories.fromId(budget.category)
    val iconTint = category?.color ?: MaterialTheme.colorScheme.primary
    val iconBackground = iconTint.copy(alpha = 0.12f)

    val progressColor = when {
        budget.isOverBudget -> ProgressError
        budget.isWarning -> Warning
        else -> iconTint
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(iconBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        category?.let {
                            Icon(
                                imageVector = ImageVector.vectorResource(it.icon),
                                contentDescription = null,
                                tint = iconTint,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = category?.let { stringResource(it.nameResId) }
                                ?: budget.category,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${budget.currencySymbol} ${
                                String.format(
                                    "%.0f",
                                    budget.spent
                                )
                            } of ${budget.currencySymbol} ${String.format("%.0f", budget.amount)}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    if (budget.isOverBudget) {
                        StatusBadge(
                            text = "Over Budget",
                            color = ProgressError
                        )
                    } else if (budget.isWarning) {
                        StatusBadge(
                            text = "Almost Full",
                            color = Warning
                        )
                    }

                    Text(
                        text = if (budget.isOverBudget) {
                            "${budget.currencySymbol} ${
                                String.format(
                                    "%.0f",
                                    budget.overBudget
                                )
                            } over"
                        } else {
                            "${budget.currencySymbol} ${
                                String.format(
                                    "%.0f",
                                    budget.remaining
                                )
                            } left"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = progressColor
                    )
                }
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
                        text = "${String.format("%.1f", budget.percentage * 100)}%",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${budget.currencySymbol}${String.format("%.0f", budget.amount)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(
    text: String,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.1f),
        modifier = modifier.padding(bottom = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Warning,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = color
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = color
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
                userId = ""
            ),
            onClick = {}
        )
    }

}
