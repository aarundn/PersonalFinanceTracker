package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.core.ui.theme.AppColorTokens
import com.example.core.ui.theme.AppTheme
import com.example.personalfinancetracker.features.home.HomeTransactionUiModel

@Composable
fun HomeTransactionCard(
    transaction: HomeTransactionUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryColor = Color(transaction.colorHex)
    val amountColor = if (transaction.isIncome) AppColorTokens.Income else AppColorTokens.Expense

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusLarge))
            .background(Color(0xFF161616)) // Dark card background
            .clickable(onClick = onClick)
            .padding(AppTheme.dimensions.spacingMedium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left side: Amount and Time (End in RTL)
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = transaction.amount,
                style = MaterialTheme.typography.titleMedium,
                color = amountColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = transaction.time,
                style = MaterialTheme.typography.bodySmall,
                color = AppColorTokens.Gray400
            )
        }

        // Right side: Icon and Titles (Start in RTL)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = AppColorTokens.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = transaction.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColorTokens.Gray400
                )
            }
            
            Spacer(modifier = Modifier.width(AppTheme.dimensions.spacingMedium))
            
            // Icon in Squircle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, categoryColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .background(categoryColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart, // Mock icon
                    contentDescription = null,
                    tint = categoryColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
