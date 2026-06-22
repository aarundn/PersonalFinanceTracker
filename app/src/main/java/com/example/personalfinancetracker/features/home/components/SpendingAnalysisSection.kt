package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.example.core.ui.theme.AppTheme
import com.example.personalfinancetracker.features.home.BarChartItem
import com.example.personalfinancetracker.features.home.Timeframe

@Composable
fun SpendingAnalysisSection(
    timeframe: Timeframe,
    onTimeframeChanged: (Timeframe) -> Unit,
    barChartData: List<BarChartItem>,
    dailyAverage: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Spending Analysis",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            TimeframeToggle(
                selectedTimeframe = timeframe,
                onTimeframeSelected = onTimeframeChanged,
                modifier = Modifier.width(AppTheme.dimensions.iconSizeHuge * 2 + AppTheme.dimensions.spacingLarge)
            )
        }
        
        Spacer(modifier = Modifier.height(AppTheme.dimensions.spacingMedium))
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(AppTheme.dimensions.radiusExtraLarge))
                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                .padding(AppTheme.dimensions.spacingMedium)
        ) {
            // Tooltip space

            Spacer(modifier = Modifier.height(AppTheme.dimensions.spacingSmall))

            // Bars
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                barChartData.forEach { item ->
                    val barColor = if (item.isSelected) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.surfaceContainerHigh
                    }
                    val barBgColor = MaterialTheme.colorScheme.surfaceContainer

                    ChartBar(modifier = Modifier.weight(1f),barBgColor, item, barColor)
                }
            }

            Spacer(modifier = Modifier.height(AppTheme.dimensions.spacingMedium))

            Divider(
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = AppTheme.dimensions.borderThin
            )
            Spacer(modifier = Modifier.height(AppTheme.dimensions.spacingMedium))

            // Daily Average
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daily Average",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = dailyAverage,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun ChartBar(
    modifier: Modifier,
    barBgColor: Color,
    item: BarChartItem,
    barColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .width(AppTheme.dimensions.spacingLarge)
                .height(AppTheme.dimensions.iconSizeHuge + AppTheme.dimensions.buttonHeightSmall)
                .clip(RoundedCornerShape(AppTheme.dimensions.spacingSmall))
                .background(barBgColor),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(item.value)
                    .clip(RoundedCornerShape(AppTheme.dimensions.spacingSmall))
                    .background(barColor)
            )
        }
        
        Spacer(modifier = Modifier.height(AppTheme.dimensions.spacingMediumSmall))
        
        Text(
            text = item.label,
            style = MaterialTheme.typography.bodySmall,
            color = if (item.isSelected) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            textAlign = TextAlign.Center
        )
        
        if (item.isSelected) {
            Spacer(modifier = Modifier.height(AppTheme.dimensions.spacingExtraSmall))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(AppTheme.dimensions.radiusMedium))
                    .background(MaterialTheme.colorScheme.onSurface)
                    .padding(
                        horizontal = AppTheme.dimensions.spacingSmall,
                        vertical = AppTheme.dimensions.spacingExtraSmall
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.amountString,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.surface,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
