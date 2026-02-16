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
import com.example.core.model.DefaultCategories
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.ui.theme.ProgressError
import com.example.personalfinancetracker.features.budget.common.BudgetPeriodOptions
import com.example.personalfinancetracker.features.budget.edit_budget.EditBudgetState
import com.example.personalfinancetracker.features.budget.model.BudgetUi
import kotlin.math.max

@Composable
fun BudgetInsightsCard(
    state: EditBudgetState,
    modifier: Modifier = Modifier
) {
    val budget = state.budget ?: return
    
    val daysInPeriod = BudgetPeriodOptions.findById(budget.period).days
    val daysElapsed = calculateDaysElapsed(budget.createdAt, daysInPeriod)
    val averageDailySpend = if (daysElapsed > 0) budget.spent / daysElapsed else 0.0
    val projectedTotal = if (daysElapsed > 0) averageDailySpend * daysInPeriod else budget.spent
    
    val projectedColor = if (projectedTotal > budget.amount) {
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
                value = "$daysElapsed of $daysInPeriod"
            )

            InsightRow(
                label = "Average daily spending",
                value = "$${averageDailySpend.formatCurrency()}",
                emphasize = true
            )

            InsightRow(
                label = "Projected ${periodLabel(state.periodInput)} total",
                value = "$${projectedTotal.formatCurrency()}",
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
    BudgetPeriodOptions.findById(id).label
    
private fun calculateDaysElapsed(createdAt: Long, daysInPeriod: Int): Int {
    val diff = System.currentTimeMillis() - createdAt
    val days = (diff / (1000 * 60 * 60 * 24)).toInt()
    return max(0, minOf(days, daysInPeriod))
}

@Preview(showBackground = true)
@Composable
private fun BudgetInsightsCardPreview() {
    PersonalFinanceTrackerTheme {
        BudgetInsightsCard(
            state = EditBudgetState(
                budget = BudgetUi(
                    id = "1",
                    userId = "A1",
                    category = DefaultCategories.FOOD.id,
                    amount = 500.0,
                    currency = "USD",
                    period = BudgetPeriodOptions.Monthly.id,
                    notes = null,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    spent = 320.0
                ),
                amountInput = "500.00",
                periodInput = BudgetPeriodOptions.Monthly.id
            )
        )
    }
}

