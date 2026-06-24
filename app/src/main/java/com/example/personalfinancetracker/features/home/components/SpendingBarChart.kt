package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.AppTheme
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.personalfinancetracker.features.home.BarChartItem

@Composable
fun SpendingBarChart(
    barChartData: List<BarChartItem>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
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

            ChartBar(
                modifier = Modifier.weight(1f),
                barBgColor = barBgColor,
                item = item,
                barColor = barColor
            )
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
        // The Bar
        Box(
            modifier = Modifier
                .width(AppTheme.dimensions.spacingExtraLarge)
                .height(AppTheme.dimensions.iconSizeHuge + AppTheme.dimensions.buttonHeightSmall)
                .clip(
                    RoundedCornerShape(
                        topEnd = AppTheme.dimensions.spacingSmall,
                        topStart = AppTheme.dimensions.spacingSmall
                    )
                )
                .background(barBgColor),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(item.value)
                    .clip(
                        RoundedCornerShape(
                            topEnd = AppTheme.dimensions.spacingSmall,
                            topStart = AppTheme.dimensions.spacingSmall
                        )
                    )
                    .background(barColor)
            )
        }

        // Space for the selected item pill to ensure all bars stay aligned
        Box(
            modifier = Modifier
                .height(36.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (item.isSelected) {
                Box(
                    modifier = Modifier
                        .wrapContentWidth(unbounded = true)
                        .shadow(
                            elevation = AppTheme.dimensions.spacingMediumSmall,
                            shape = RoundedCornerShape(AppTheme.dimensions.radiusMedium),
                            ambientColor = Color.White.copy(alpha = 0.5f),
                            spotColor = Color.White.copy(alpha = 0.5f)
                        )
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
                        maxLines = 1,
                        softWrap = false,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        ),
                        color = MaterialTheme.colorScheme.surface,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Day name
        Text(
            text = item.label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = if (item.isSelected) FontWeight.Bold else FontWeight.Normal
            ),
            color = if (item.isSelected) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun SpendingAnalysis() {
    PersonalFinanceTrackerTheme {
        SpendingBarChart(
            barChartData = listOf(
                BarChartItem(label = "Mon", value = 0.2f, amountString = "$20"),
                BarChartItem(label = "Tue", value = 0.5f, amountString = "$50"),
                BarChartItem(label = "Wed", value = 0.8f, amountString = "$80", isSelected = true),
                BarChartItem(label = "Thu", value = 0.3f, amountString = "$30"),
                BarChartItem(label = "Fri", value = 0.6f, amountString = "$60"),
                BarChartItem(label = "Sat", value = 0.4f, amountString = "$40"),
                BarChartItem(label = "Sun", value = 0.7f, amountString = "$70")
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}