package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.R
import com.example.core.ui.theme.Expense
import com.example.core.ui.theme.Income
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.ui.theme.PrimaryLight
import com.example.personalfinancetracker.features.home.HomeContract

@Composable
fun OverviewRow(
    state: HomeContract.State,
    modifier: Modifier = Modifier
) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MoneyCard(
            title = state.income.label,
            value = state.income.amount,
            icon = ImageVector.vectorResource(R.drawable.trending_up),
            modifier = Modifier.weight(1f),
            backgroundColor = Income.copy(alpha = 0.1f),
            borderColor = Income.copy(alpha = 0.2f),
            textColor = Income
        )
        MoneyCard(
            title = state.expenses.label,
            value = state.expenses.amount,
            icon = ImageVector.vectorResource(R.drawable.trending_down),
            modifier = Modifier.weight(1f),
            backgroundColor = Expense.copy(alpha = 0.1f),
            borderColor = Expense.copy(alpha = 0.2f),
            textColor = Expense
        )
        MoneyCard(
            title = state.balance.label,
            value = state.balance.amount,
            icon = ImageVector.vectorResource(R.drawable.bank),
            modifier = Modifier.weight(1f),
            backgroundColor = PrimaryLight.copy(alpha = 0.1f),
            borderColor = PrimaryLight.copy(alpha = 0.2f),
            textColor = PrimaryLight
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OverviewRowPreview() {
    PersonalFinanceTrackerTheme {
        OverviewRow(
            state = HomeContract.State()
        )
    }
}
