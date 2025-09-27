package com.example.personalfinancetracker.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.personalfinancetracker.features.home.HomeContract.BudgetProgress
import com.example.personalfinancetracker.features.home.HomeContract.Event
import com.example.personalfinancetracker.features.home.HomeContract.State
import com.example.personalfinancetracker.features.home.components.BudgetSummaryCard
import com.example.personalfinancetracker.features.home.components.HeaderSection
import com.example.personalfinancetracker.features.home.components.MonthCard
import com.example.personalfinancetracker.features.home.components.OverviewRow
import com.example.personalfinancetracker.features.home.components.QuickActions
import com.example.core.ui.theme.PersonalFinanceTrackerTheme

@Composable
fun HomeScreen(
    state: State,
    onEvent: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(PaddingValues(horizontal = 16.dp))
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderSection(state)
        OverviewRow(state)
        MonthCard(state)
        BudgetSummaryCard(state, onEvent)
        QuickActions(onEvent)
    }
}


@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    PersonalFinanceTrackerTheme {
        HomeScreen(
            state = State(
                budgets = listOf(
                    BudgetProgress("Food", 385.0, 500.0),
                    BudgetProgress("Transport", 265.0, 300.0),
                    BudgetProgress("Shopping", 520.0, 400.0),
                    BudgetProgress("Bills", 420.0, 600.0)
                )
            ),
            onEvent = {}
        )
    }
}