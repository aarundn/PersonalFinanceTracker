package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.example.core.ui.theme.AppTheme
import com.example.core.ui.theme.withLightAlpha

@Composable
fun BalanceCard(
    totalBalance: String,
    percentageChange: String,
    onAddClick: () -> Unit,
    onTransferClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val brush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surfaceContainerHighest,
            MaterialTheme.colorScheme.surfaceContainer
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusExtraLarge))
            .background(brush)
            .padding(AppTheme.dimensions.spacingMedium),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // Title
            Text(
                text = "Total Balance",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Percentage Chip
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(AppTheme.dimensions.radiusMedium))
                    .background(AppTheme.colors.income.withLightAlpha())
                    .padding(
                        horizontal = AppTheme.dimensions.spacingSmall,
                        vertical = AppTheme.dimensions.spacingExtraSmall
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowUpward,
                    contentDescription = null,
                    tint = AppTheme.colors.income,
                    modifier = Modifier.size(AppTheme.dimensions.iconSizeSmall)
                )
                Spacer(modifier = Modifier.width(AppTheme.dimensions.spacingExtraSmall))
                Text(
                    text = percentageChange,
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.colors.income,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(AppTheme.dimensions.spacingMedium))

        // Balance Amount
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "SAR",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = AppTheme.dimensions.spacingSmall)
            )
            Spacer(modifier = Modifier.width(AppTheme.dimensions.spacingSmall))
            Text(
                text = totalBalance,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(AppTheme.dimensions.spacingLarge))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacingMediumSmall)
        ) {
            // Transfer Button (Dark)
            QuickActionButton(
                text = "Transfer",
                icon = Icons.Default.Send,
                onClick = onTransferClick,
                isPrimary = false,
                modifier = Modifier.weight(1f)
            )

            // Add Button (Light)
            QuickActionButton(
                text = "Add",
                icon = Icons.Default.Add,
                onClick = onAddClick,
                isPrimary = true,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QuickActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false
) {
    val backgroundColor = if (isPrimary) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceContainerHigh
    }
    val contentColor = if (isPrimary) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusLarge))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = AppTheme.dimensions.spacingMedium),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(AppTheme.dimensions.iconSizeNormal)
        )
        Spacer(modifier = Modifier.width(AppTheme.dimensions.spacingSmall))
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = contentColor,
            fontWeight = FontWeight.Bold
        )
    }
}
