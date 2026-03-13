package com.example.core.components

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.core.model.BudgetDisplayData
import com.example.core.ui.theme.ProgressError
import com.example.core.ui.theme.Warning

@Composable
fun BudgetSelectorCard(
    budget: BudgetDisplayData,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val progressColor = when {
        budget.isOverBudget -> ProgressError
        budget.isWarning -> Warning
        else -> budget.iconTint
    }

    val borderColor = if (isSelected) budget.iconTint else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    val borderWidth = if (isSelected) 2.dp else 1.dp

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = BorderStroke(borderWidth, borderColor),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BudgetInfo(
                modifier = Modifier.weight(1f),
                iconTint = budget.iconTint,
                iconBackground = budget.iconBackground,
                icon = budget.icon,
                categoryName = budget.categoryName,
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
    }
}

@SuppressLint("DefaultLocale")
fun Double.formatCurrency(): String = String.format("%,.2f", this)