package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.core.ui.theme.AppColorTokens
import com.example.core.ui.theme.AppTheme
import com.example.personalfinancetracker.features.home.BudgetUiModel

@Composable
fun BudgetsSection(
    budgets: List<BudgetUiModel>,
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
                color = AppColorTokens.White
            )
            Text(
                text = "View All",
                style = MaterialTheme.typography.bodyMedium,
                color = AppColorTokens.Gray400,
                modifier = Modifier.clickable(onClick = onViewAllClick)
            )
        }

        Spacer(modifier = Modifier.height(AppTheme.dimensions.spacingMedium))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacingMedium),
            contentPadding = PaddingValues(end = AppTheme.dimensions.spacingMedium) // Extra padding for edge
        ) {
            items(budgets) { budget ->
                BudgetCard(
                    budget = budget,
                    onClick = { onBudgetClick(budget.id) },
                    modifier = Modifier.width(160.dp) // Fixed width to match Figma
                )
            }
        }
    }
}

@Composable
private fun BudgetCard(
    budget: BudgetUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val budgetColor = Color(budget.colorHex)

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusLarge))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            .clickable(onClick = onClick)
            .padding(AppTheme.dimensions.spacingMedium),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left side Icon with circular progress
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(48.dp)
        ) {
            CircularProgressIndicator(
                progress = budget.percentage,
                modifier = Modifier.fillMaxSize(),
                color = budgetColor,
                trackColor = budgetColor.copy(alpha = 0.2f),
                strokeWidth = 4.dp
            )
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(budgetColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                // Mock icon
                Icon(
                    imageVector = Icons.Default.ShoppingCart, // Placeholder, would use real category icon
                    contentDescription = null,
                    tint = budgetColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(AppTheme.dimensions.spacingMedium))

        // Right side texts
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = budget.categoryName,
                style = MaterialTheme.typography.bodyMedium,
                color = AppColorTokens.White
            )
            Text(
                text = "${(budget.percentage * 100).toInt()}%", // Simple formatting for mock
                style = MaterialTheme.typography.titleMedium,
                color = AppColorTokens.White
            )
        }
    }
}
