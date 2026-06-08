package com.example.personalfinancetracker.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.core.components.EmptyState
import com.example.core.components.LoadingIndicator
import com.example.core.ui.theme.AppTheme
import com.example.personalfinancetracker.features.home.components.BalanceCard
import com.example.personalfinancetracker.features.home.components.BudgetsSection
import com.example.personalfinancetracker.features.home.components.HomeHeader
import com.example.personalfinancetracker.features.home.components.SpendingAnalysisSection
import com.example.personalfinancetracker.features.home.components.TransactionsList

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
                title = "Something went wrong",
                description = homeUiState.message.asString(),
                buttonText = "Retry",
                onAddClick = { onEvent(HomeEvent.OnLoadHomeData) },
                modifier = modifier
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
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = AppTheme.dimensions.spacingMedium,
                end = AppTheme.dimensions.spacingMedium,
                top = AppTheme.dimensions.spacingLarge,
                bottom = AppTheme.dimensions.spacingExtraLarge
            ),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacingLarge)
        ) {
            item {
                HomeHeader(userName = data.userName)
            }

            item {
                BalanceCard(
                    totalBalance = data.totalBalance,
                    percentageChange = data.balancePercentageChange,
                    onAddClick = { onEvent(HomeEvent.OnClickAddTransaction) },
                    onTransferClick = { onEvent(HomeEvent.OnClickTransfer) }
                )
            }

            item {
                SpendingAnalysisSection(
                    timeframe = data.selectedAnalysisTimeframe,
                    onTimeframeChanged = { onEvent(HomeEvent.OnAnalysisTimeframeChanged(it)) },
                    barChartData = data.barChartData,
                    dailyAverage = data.dailyAverage
                )
            }

            item {
                BudgetsSection(
                    budgets = data.budgets,
                    onViewAllClick = { onEvent(HomeEvent.OnClickViewAllBudgets) },
                    onBudgetClick = { onEvent(HomeEvent.OnClickBudget(it)) }
                )
            }

            item {
                TransactionsList(
                    transactions = data.recentTransactions,
                    onViewAllClick = { onEvent(HomeEvent.OnClickViewAllTransactions) },
                    onTransactionClick = { onEvent(HomeEvent.OnClickTransaction(it)) }
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(AppTheme.dimensions.spacingExtraLarge))
            }
        }
    }
}