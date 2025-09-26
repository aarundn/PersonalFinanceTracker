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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.personalfinancetracker.features.budget.budgets.BudgetContract
import com.example.personalfinancetracker.ui.components.CustomProgressBar
import com.example.personalfinancetracker.ui.theme.ProgressError
import com.example.personalfinancetracker.ui.theme.Warning

@Composable
fun BudgetCard(
    budget: BudgetContract.Budget,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icon
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(budget.iconTint.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = budget.icon,
                            contentDescription = null,
                            tint = budget.iconTint,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Category and Amount
                    Column {
                        Text(
                            text = budget.category,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "$${String.format("%.0f", budget.spent)} of $${String.format("%.0f", budget.budgeted)}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Status and Remaining
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    // Status Badge
                    if (budget.isOverBudget) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ProgressError.copy(alpha = 0.1f),
                            modifier = Modifier.padding(bottom = 4.dp)
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
                                    tint = ProgressError
                                )
                                Text(
                                    text = "Over Budget",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ProgressError
                                )
                            }
                        }
                    } else if (budget.isWarning) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Warning.copy(alpha = 0.1f), // Yellow-100
                            modifier = Modifier.padding(bottom = 4.dp)
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
                                    tint = Warning
                                )
                                Text(
                                    text = "Almost Full",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Warning
                                )
                            }
                        }
                    }

                    // Remaining/Over amount
                    Text(
                        text = if (budget.isOverBudget) {
                            "$${String.format("%.0f", budget.overBudget)} over"
                        } else {
                            "$${String.format("%.0f", budget.remaining)} left"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = if (budget.isOverBudget)
                            ProgressError
                        else if (budget.isWarning)
                            Warning
                        else
                            MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Progress Bar
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                CustomProgressBar(
                    progress = budget.percentage.coerceIn(0f, 1f),
                    modifier = Modifier.fillMaxWidth(),
                    progressColor = if (budget.isOverBudget)
                        ProgressError
                    else if (budget.isWarning)
                        Warning
                    else
                        MaterialTheme.colorScheme.primary,
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
                        text = "$${String.format("%.0f", budget.budgeted)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
