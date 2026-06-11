package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.core.ui.theme.AppTheme
import com.example.personalfinancetracker.features.budget.model.BudgetUi

@Composable
fun BudgetsSection(
    budgets: List<BudgetUi>,
    onViewAllClick: () -> Unit,
    onBudgetClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "Budgets",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "View All",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.clickable(onClick = onViewAllClick)
            )
        }

        Spacer(modifier = Modifier.height(AppTheme.dimensions.spacingMedium))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacingMedium),
            contentPadding = PaddingValues(end = AppTheme.dimensions.spacingMedium)
        ) {
            items(budgets, key = { it.id }) { budget ->
                HomeBudgetCard(
                    budget = budget,
                    onClick = { onBudgetClick(budget.id) },
                    modifier = Modifier.width(AppTheme.dimensions.iconSizeHuge * 3)
                )
            }
        }
    }
}