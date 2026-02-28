package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.core.components.CustomProgressBar
import com.example.core.ui.theme.ProgressError
import com.example.core.ui.theme.ProgressPrimary
import com.example.personalfinancetracker.features.budget.model.BudgetUi
import com.example.personalfinancetracker.features.home.utils.TextFormattingUtils

@Composable
fun BudgetSummaryCard(
    budgets: List<BudgetUi>,
    currencySymbol: String,
    onBudgetClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (budgets.isEmpty()) return

    val averageUsed = budgets.map { it.percentage }.average().toFloat()

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Outlined.CheckCircle, contentDescription = null)
                    Text("Budget Summary", style = MaterialTheme.typography.titleMedium)
                }
                AssistChip(
                    onClick = {},
                    label = {
                        Text("${TextFormattingUtils.formatPercentage(averageUsed.toDouble())} used")
                    }
                )
            }
            budgets.forEach { budget ->
                BudgetItem(
                    budget = budget,
                    currencySymbol = currencySymbol,
                    onClick = { onBudgetClick(budget.id) }
                )
            }
        }
    }
}

@Composable
private fun BudgetItem(
    budget: BudgetUi,
    currencySymbol: String,
    onClick: () -> Unit
) {
    val category = budget.currentCategory

    Column(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(category.color.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(category.icon),
                        contentDescription = null,
                        tint = category.color,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Column {
                    Text(
                        text = budget.category,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = TextFormattingUtils.formatBudgetProgress(
                            budget.spent,
                            budget.amount,
                            currencySymbol
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (budget.isOverBudget) {
                Icon(Icons.Outlined.Warning, contentDescription = null, tint = ProgressError)
            }
        }
        Spacer(Modifier.height(8.dp))
        CustomProgressBar(
            progress = budget.percentage.coerceAtMost(1.0f),
            modifier = Modifier.fillMaxWidth(),
            progressColor = if (budget.isOverBudget) ProgressError else ProgressPrimary
        )
    }
}
