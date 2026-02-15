package com.example.core.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.core.ui.theme.ProgressError
import com.example.core.ui.theme.Warning

@Composable
fun BudgetAmountStateInfo(
    isOverBudget: Boolean,
    isWarning: Boolean,
    progressColor: Color,
    overBudget: String,
    remaining: String,
    currencySymbol: String,

) {
    Column(
        horizontalAlignment = Alignment.End
    ) {
        if (isOverBudget) {
            BudgetStatusBadge(
                text = "Over Budget",
                color = ProgressError
            )
        } else if (isWarning) {
            BudgetStatusBadge(
                text = "Almost Full",
                color = Warning
            )
        }

        Text(
            text = if (isOverBudget) {
                "$currencySymbol $overBudget over"
            } else {
                "$currencySymbol $remaining left"
            },
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium
            ),
            color = progressColor
        )
    }
}