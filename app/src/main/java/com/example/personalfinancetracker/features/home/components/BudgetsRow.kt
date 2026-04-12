package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.core.R
import com.example.core.components.EmptyState
import com.example.core.ui.theme.dimensions

import com.example.personalfinancetracker.features.budget.model.BudgetUi

@Composable
fun BudgetsRow(
    modifier: Modifier = Modifier,
    budgets: List<BudgetUi>,
    onBudgetClick: (String) -> Unit,
    onAddBudgetClick: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(R.string.recent_budgets),
            style = MaterialTheme.typography.titleMedium
        )
        if (budgets.isEmpty()) {
            EmptyState(
                title = stringResource(R.string.budget_empty_title),
                description = stringResource(R.string.budget_empty_description),
                buttonText = stringResource(R.string.action_add_budget),
                onAddClick = onAddBudgetClick
            )
        } else {
            Spacer(modifier = Modifier.padding(MaterialTheme.dimensions.spacingSmall))
            LazyRow(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.spacingMediumSmall)
            ) {
                items(budgets.size) {
                    HomeBudgetCard(
                        budget = budgets[it],
                        modifier = Modifier.clickable { onBudgetClick(budgets[it].id) }
                    )
                }
            }
        }

    }

}
