package com.example.core.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.core.R
import com.example.core.ui.theme.AppTheme
import com.example.core.utils.formatAmountLeft
import com.example.core.utils.formatAmountOver

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
                text = stringResource(R.string.budget_over_budget),
                color = AppTheme.colors.expense
            )
        } else if (isWarning) {
            BudgetStatusBadge(
                text = stringResource(R.string.budget_almost_full),
                color = AppTheme.colors.warning
            )
        }

        Text(
            text = if (isOverBudget) {
                formatAmountOver(currencySymbol, overBudget)
            } else {
                formatAmountLeft(currencySymbol, remaining)
            },
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium
            ),
            color = progressColor
        )
    }
}