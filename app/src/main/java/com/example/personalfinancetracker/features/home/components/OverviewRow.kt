package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.personalfinancetracker.features.home.HomeContract
import com.example.personalfinancetracker.ui.theme.Expense
import com.example.personalfinancetracker.ui.theme.Income
import com.example.personalfinancetracker.ui.theme.PersonalFinanceTrackerTheme
import com.example.personalfinancetracker.ui.theme.PrimaryLight

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
            icon = Icons.Outlined.Check,
            modifier = Modifier.weight(1f),
            backgroundColor = Income.copy(alpha = 0.1f),
            borderColor = Income.copy(alpha = 0.2f),
            textColor = Income
        )
        MoneyCard(
            title = state.expenses.label,
            value = state.expenses.amount,
            icon = Icons.Outlined.Clear,
            modifier = Modifier.weight(1f),
            backgroundColor = Expense.copy(alpha = 0.1f),
            borderColor = Expense.copy(alpha = 0.2f),
            textColor = Expense
        )
        MoneyCard(
            title = state.balance.label,
            value = state.balance.amount,
            icon = Icons.Outlined.ShoppingCart,
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
