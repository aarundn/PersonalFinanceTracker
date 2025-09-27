package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.theme.CategoryBills
import com.example.core.ui.theme.CategoryFood
import com.example.core.ui.theme.CategoryShopping
import com.example.core.ui.theme.CategoryTransport
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.ui.theme.ProgressError
import com.example.core.ui.theme.ProgressPrimary
import com.example.personalfinancetracker.features.home.HomeContract
import com.example.personalfinancetracker.features.home.utils.TextFormattingUtils

@Composable
fun BudgetSummaryCard(
    state: HomeContract.State,
    onEvent: (HomeContract.Event) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Outlined.CheckCircle, contentDescription = null)
                    Text("Budget Summary", style = MaterialTheme.typography.titleMedium)
                }
                AssistChip(
                    onClick = {},
                    label = { Text("78.5% used") },
                    colors = AssistChipDefaults.assistChipColors()
                )
            }
            state.budgets.forEach { BudgetItem(it) { onEvent(HomeContract.Event.OnClickBudgetItem(it.name)) } }
        }
    }
}

@Composable
private fun BudgetItem(item: HomeContract.BudgetProgress, onClick: () -> Unit) {
    val isOverBudget = item.progress > 1.0f
    val categoryColors = mapOf(
        "Food" to CategoryFood,
        "Transport" to CategoryTransport,
        "Shopping" to CategoryShopping,
        "Bills" to CategoryBills
    )
    val categoryIcons = mapOf(
        "Food" to Icons.Outlined.ShoppingCart,
        "Transport" to Icons.Outlined.Warning,
        "Shopping" to Icons.Outlined.ShoppingCart,
        "Bills" to Icons.Outlined.Home
    )
    
    Column(Modifier
        .fillMaxWidth()
        .clickable(onClick = onClick)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
            ) {
                val categoryColor = categoryColors[item.name] ?: Color.Gray
                val categoryIcon = categoryIcons[item.name] ?: Icons.Outlined.ShoppingCart
                
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(
                            categoryColor.copy(alpha = 0.1f),
                            androidx.compose.foundation.shape.CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        categoryIcon, 
                        contentDescription = null, 
                        tint = categoryColor,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Column {
                    Text(item.name, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        TextFormattingUtils.formatBudgetProgress(item.spent, item.limit),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (isOverBudget) {
                Icon(Icons.Outlined.Warning, contentDescription = null, tint = ProgressError)
            }
        }
        Spacer(Modifier.height(8.dp))
        CustomProgressBar(
            progress = item.progress.coerceAtMost(1.0f),
            modifier = Modifier.fillMaxWidth(),
            progressColor = if (isOverBudget) ProgressError else ProgressPrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BudgetSummaryCardPreview() {
    PersonalFinanceTrackerTheme {
        BudgetSummaryCard(
            state = HomeContract.State(
                budgets = listOf(
                    HomeContract.BudgetProgress("Food", 385.0, 500.0),
                    HomeContract.BudgetProgress("Transport", 265.0, 300.0)
                )
            ),
            onEvent = {}
        )
    }
}
