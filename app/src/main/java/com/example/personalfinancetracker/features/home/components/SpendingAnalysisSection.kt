package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
            SpendingBarChart(
                barChartData = barChartData,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(AppTheme.dimensions.spacingMedium))

            HorizontalDivider(
                Modifier,
                thickness = AppTheme.dimensions.borderThin,
                color = MaterialTheme.colorScheme.outlineVariant
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
