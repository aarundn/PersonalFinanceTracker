package com.example.personalfinancetracker.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.core.components.EmptyState
import com.example.core.components.LoadingIndicator
import com.example.personalfinancetracker.features.home.HomeContract.Event
import com.example.personalfinancetracker.features.home.HomeContract.HomeData
import com.example.personalfinancetracker.features.home.HomeContract.HomeUiState
import com.example.personalfinancetracker.features.home.components.BudgetSummaryCard
import com.example.personalfinancetracker.features.home.components.MonthCard
import com.example.personalfinancetracker.features.home.components.OverviewRow
import com.example.personalfinancetracker.features.home.components.QuickActions

@Composable
fun HomeScreen(
    homeUiState: HomeUiState,
    onEvent: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    when (homeUiState) {
        HomeUiState.Loading -> {
            LoadingIndicator(modifier = modifier.fillMaxSize())
        }

        is HomeUiState.Error -> {
            EmptyState(
                title = "Something went wrong",
                description = homeUiState.message,
                buttonText = "Retry",
                onAddClick = { onEvent(Event.OnRetry) },
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
    onEvent: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(PaddingValues(horizontal = 16.dp))
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Greeting header (inline — home tab has no TopAppBar back button)
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

        OverviewRow(data = data)
        MonthCard(data = data)
        BudgetSummaryCard(data = data, onEvent = onEvent)
        QuickActions(onEvent = onEvent)
    }
}