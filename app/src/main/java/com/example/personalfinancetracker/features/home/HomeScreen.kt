package com.example.personalfinancetracker.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.core.R
import com.example.core.components.EmptyState
import com.example.core.components.LoadingIndicator
import com.example.core.ui.theme.AppTheme
import com.example.personalfinancetracker.features.home.components.BudgetsRow
import com.example.personalfinancetracker.features.home.components.MonthCard
import com.example.personalfinancetracker.features.transaction.transactions.components.TransactionCard

@Composable
fun HomeScreen(
    homeUiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    when (homeUiState) {
        HomeUiState.Loading -> {
            LoadingIndicator(modifier = modifier.fillMaxSize())
        }

        is HomeUiState.Error -> {
            EmptyState(
                title = stringResource(R.string.home_something_went_wrong),
                description = homeUiState.message.asString(),
                buttonText = stringResource(R.string.action_retry),
                onAddClick = { onEvent(HomeEvent.OnRetry) },
                modifier = modifier,
                secondaryButtonText = stringResource(R.string.home_go_to_settings),
                onSecondaryClick = { onEvent(HomeEvent.OnClickSettings) }
            )
        }

        is HomeUiState.Success -> {
            HomeContent(
                data = homeUiState.data,
                onEvent = onEvent,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun HomeContent(
    data: HomeData,
    onEvent: (HomeEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(PaddingValues(horizontal = AppTheme.dimensions.spacingMedium))
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacingMediumSmall)
    ) {
        item (){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = data.greeting,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = data.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = { onEvent(HomeEvent.OnClickSettings) }) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = stringResource(R.string.cd_settings),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Spacer(modifier = Modifier.padding(AppTheme.dimensions.spacingSmall))
            MonthCard(
                totalTransactions = data.totalTransactions,
                dailyAverage = data.dailyAverage,
                daysPassed = data.daysPassed,
                daysInMonth = data.daysInMonth,
                currencySymbol = data.currencySymbol
            )
            Spacer(modifier = Modifier.padding(AppTheme.dimensions.spacingSmall))
            BudgetsRow(
                budgets = data.budgets,
                onBudgetClick = { budgetId -> onEvent(HomeEvent.OnClickBudgetItem(budgetId)) },
                onAddBudgetClick = { onEvent(HomeEvent.OnClickAddBudget) }
            )
            Spacer(modifier = Modifier.padding(AppTheme.dimensions.spacingMediumSmall))
            Text(
                text = stringResource(R.string.recent_transactions),
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (data.transactions.isEmpty()) {
            item {
                EmptyState(
                    title = stringResource(R.string.transaction_empty_title),
                    description = stringResource(R.string.transaction_empty_description),
                    buttonText = stringResource(R.string.transaction_add_button),
                    onAddClick = { onEvent(HomeEvent.OnClickAddExpense) },
                    modifier = Modifier.padding(vertical = AppTheme.dimensions.spacingMedium)
                )
            }
        } else {
            items(data.transactions.size) {
                TransactionCard(
                    transaction = data.transactions[it],
                    modifier = Modifier,
                    onClick = {}
                )
            }
        }
        item { Spacer(modifier = Modifier.padding(AppTheme.dimensions.spacingSmall))}
    }
}