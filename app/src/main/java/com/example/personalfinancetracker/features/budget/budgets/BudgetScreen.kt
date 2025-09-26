package com.example.personalfinancetracker.features.budget.budgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.personalfinancetracker.features.budget.budgets.components.BudgetCard
import com.example.personalfinancetracker.features.budget.budgets.components.EmptyState
import com.example.personalfinancetracker.features.budget.budgets.components.HeaderSection
import com.example.personalfinancetracker.features.budget.budgets.components.OverallSummaryCard

@Composable
fun BudgetScreen(
    state: BudgetContract.State,
    onEvent: (BudgetContract.Event) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            state.budgets.isEmpty() -> {
                EmptyState(
                    onAddClick = { onEvent(BudgetContract.Event.OnAddBudgetClick) }
                )
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    HeaderSection(
                        title = "Budget Overview",
                        onAddClick = { onEvent(BudgetContract.Event.OnAddBudgetClick) }
                    )

                    // Overall Summary Card
                    OverallSummaryCard(
                        totalBudgeted = state.totalBudgeted,
                        totalSpent = state.totalSpent,
                        overallProgress = state.overallProgress
                    )

                    // Budget Categories List
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 88.dp) // Space for FAB
                    ) {
                        items(state.budgets) { budget ->
                            BudgetCard(
                                budget = budget,
                                onClick = { onEvent(BudgetContract.Event.OnBudgetClick(budget)) }
                            )
                        }
                    }
                }

                // Floating Action Button
                FloatingActionButton(
                    onClick = { onEvent(BudgetContract.Event.OnAddBudgetClick) },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(24.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Budget"
                    )
                }
            }
        }

        // Error Handling
        state.error?.let { error ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
            ) {
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
