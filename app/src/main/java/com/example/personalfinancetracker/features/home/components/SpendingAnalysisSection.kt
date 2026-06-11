package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
                    .fillMaxWidth()
                    .height(AppTheme.dimensions.iconSizeHuge + AppTheme.dimensions.buttonHeightSmall),
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

                    ChartBar(barBgColor, item, barColor)
                }
            }

            Spacer(modifier = Modifier.height(AppTheme.dimensions.spacingMediumSmall))

            // X-Axis Labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                barChartData.forEach { item ->
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (item.isSelected) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppTheme.dimensions.spacingMedium))
            val selectedItem = barChartData.find { it.isSelected }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppTheme.dimensions.spacingLarge + AppTheme.dimensions.spacingSmall),
                contentAlignment = Alignment.Center
            ) {
                if (selectedItem != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(AppTheme.dimensions.radiusMedium))
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(
                                horizontal = AppTheme.dimensions.spacingMediumSmall,
                                vertical = AppTheme.dimensions.spacingExtraSmall
                            )
                    ) {
                        Text(
                            text = selectedItem.amountString,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
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
private fun RowScope.ChartBar(
    barBgColor: androidx.compose.ui.graphics.Color,
    item: BarChartItem,
    barColor: androidx.compose.ui.graphics.Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)
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
    }
}
