package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.core.ui.theme.Expense
import com.example.core.ui.theme.Income
import com.example.core.ui.theme.PrimaryLight
import androidx.compose.material3.MaterialTheme
import com.example.core.R
import com.example.core.ui.theme.dimensions

@Composable
fun OverviewRow(
    totalIncome: Double,
    totalExpense: Double,
    balance: Double,
    currencySymbol: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.spacingMediumSmall)
    ) {
        MoneyCard(
            title = "Income",
            value = totalIncome,
            currencySymbol = currencySymbol,
            icon = ImageVector.vectorResource(R.drawable.trending_up),
            modifier = Modifier.fillMaxWidth(),
            baseColor = Income
        )
        MoneyCard(
            title = "Expense",
            value = totalExpense,
            currencySymbol = currencySymbol,
            icon = ImageVector.vectorResource(R.drawable.trending_down),
            modifier = Modifier.fillMaxWidth(),
            baseColor = Expense
        )
        MoneyCard(
            title = "Balance",
            value = balance,
            currencySymbol = currencySymbol,
            icon = ImageVector.vectorResource(R.drawable.bank),
            modifier = Modifier.fillMaxWidth(),
            baseColor = PrimaryLight
        )
    }
}
