package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.core.ui.theme.AppTheme
import com.example.core.ui.theme.withHoverAlpha
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
            items(budgets) { budget ->
                BudgetCard(
                    budget = budget,
                    onClick = { onBudgetClick(budget.id) },
                    modifier = Modifier.width(AppTheme.dimensions.iconSizeHuge * 2 + AppTheme.dimensions.spacingExtraLarge)
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
            modifier = Modifier.size(AppTheme.dimensions.iconSizeExtraLarge)
        ) {
            CircularProgressIndicator(
                progress = budget.percentage,
                modifier = Modifier.fillMaxSize(),
                color = budgetColor,
                trackColor = budgetColor.withHoverAlpha(),
                strokeWidth = AppTheme.dimensions.borderThick
            )
            Box(
                modifier = Modifier
                    .size(AppTheme.dimensions.buttonHeightSmall)
                    .clip(CircleShape)
                    .background(budgetColor.withHoverAlpha()),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = budgetColor,
                    modifier = Modifier.size(AppTheme.dimensions.iconSizeNormal)
                )
            }
        }

        Spacer(modifier = Modifier.width(AppTheme.dimensions.spacingMedium))

        // Right side texts
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = budget.categoryName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${(budget.percentage * 100).toInt()}%",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
