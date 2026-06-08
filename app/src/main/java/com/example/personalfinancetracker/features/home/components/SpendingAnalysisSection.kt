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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.core.ui.theme.AppColorTokens
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
                color = AppColorTokens.White
            )
            
            TimeframeToggle(
                selectedTimeframe = timeframe,
                onTimeframeSelected = onTimeframeChanged,
                modifier = Modifier.width(160.dp)
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
            val selectedItem = barChartData.find { it.isSelected }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                contentAlignment = Alignment.Center
            ) {
                if (selectedItem != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(AppColorTokens.White)
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = selectedItem.amountString,
                            style = MaterialTheme.typography.labelSmall,
                            color = AppColorTokens.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bars
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                barChartData.forEach { item ->
                    val barColor = if (item.isSelected) AppColorTokens.White else MaterialTheme.colorScheme.surfaceContainerHigh
                    val barBgColor = MaterialTheme.colorScheme.surfaceContainer

                    ChartBar(barBgColor, item, barColor)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // X-Axis Labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                barChartData.forEach { item ->
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (item.isSelected) AppColorTokens.White else AppColorTokens.Gray400,
                        modifier = Modifier.weight(1f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))

            // Daily Average
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daily Average",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColorTokens.Gray400
                )
                Text(
                    text = dailyAverage,
                    style = MaterialTheme.typography.titleMedium,
                    color = AppColorTokens.White
                )
            }
        }
    }
}

@Composable
private fun RowScope.ChartBar(
    barBgColor: Color,
    item: BarChartItem,
    barColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)
    ) {
        Box(
            modifier = Modifier
                .width(24.dp)
                .height(100.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(barBgColor),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(item.value)
                    .clip(RoundedCornerShape(6.dp))
                    .background(barColor)
            )
        }
    }
}
