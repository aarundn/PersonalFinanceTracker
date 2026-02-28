package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.core.R
import com.example.core.ui.theme.Expense
import com.example.core.ui.theme.Income
import com.example.core.ui.theme.PrimaryLight
@Composable
fun OverviewRow(
    totalIncome: Double,
    totalExpense: Double,
    balance: Double,
    currencySymbol: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MoneyCard(
            title = "Income",
            value = totalIncome,
            currencySymbol = currencySymbol,
            icon = ImageVector.vectorResource(R.drawable.trending_up),
            modifier = Modifier.weight(1f),
            backgroundColor = Income.copy(alpha = 0.1f),
            borderColor = Income.copy(alpha = 0.2f),
            textColor = Income
        )
        MoneyCard(
            title = "Expense",
            value = totalExpense,
            currencySymbol = currencySymbol,
            icon = ImageVector.vectorResource(R.drawable.trending_down),
            modifier = Modifier.weight(1f),
            backgroundColor = Expense.copy(alpha = 0.1f),
            borderColor = Expense.copy(alpha = 0.2f),
            textColor = Expense
        )
        MoneyCard(
            title = "Balance",
            value = balance,
            currencySymbol = currencySymbol,
            icon = ImageVector.vectorResource(R.drawable.bank),
            modifier = Modifier.weight(1f),
            backgroundColor = PrimaryLight.copy(alpha = 0.1f),
            borderColor = PrimaryLight.copy(alpha = 0.2f),
            textColor = PrimaryLight
        )
    }
}
