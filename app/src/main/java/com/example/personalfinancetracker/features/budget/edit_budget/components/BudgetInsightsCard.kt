package com.example.personalfinancetracker.features.budget.edit_budget.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.ui.theme.ProgressError
import com.example.personalfinancetracker.features.budget.edit_budget.EditBudgetContract

@Composable
fun BudgetInsightsCard(
    state: EditBudgetContract.State,
    modifier: Modifier = Modifier
) {
    val projectedColor = if (state.projectedTotal > state.budgetedAmount) {
        ProgressError
    } else {
        MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Spending Insights",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            InsightRow(
                label = "Days into period",
                value = "${state.daysElapsed} of ${state.daysInPeriod}"
            )

            InsightRow(
                label = "Average daily spending",
                value = "$${state.averageDailySpend.formatCurrency()}",
                emphasize = true
            )

            InsightRow(
                label = "Projected ${periodLabel(state.periodInput)} total",
                value = "$${state.projectedTotal.formatCurrency()}",
                emphasize = true,
                valueColor = projectedColor
            )
        }
    }
}

@Composable
private fun InsightRow(
    label: String,
    value: String,
    emphasize: Boolean = false,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = if (emphasize) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium,
            fontWeight = if (emphasize) FontWeight.Medium else FontWeight.Normal,
            color = valueColor
        )
    }
}

private fun Double.formatCurrency(): String = String.format("%,.2f", this)

private fun periodLabel(id: String): String =
    EditBudgetContract.PeriodOptions.findById(id).label

@Preview(showBackground = true)
@Composable
private fun BudgetInsightsCardPreview() {
    PersonalFinanceTrackerTheme {
        BudgetInsightsCard(
            state = EditBudgetContract.State(
                budgetedAmountOriginal = 500.0,
                budgetedAmountInput = "500.00",
                spentAmount = 320.0,
                daysElapsed = 12,
                daysInPeriod = 30,
                periodInput = EditBudgetContract.PeriodOptions.Monthly.id
            )
        )
    }
}

