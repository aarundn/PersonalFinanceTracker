package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.core.ui.theme.AppColorTokens
import com.example.core.ui.theme.AppTheme
import com.example.personalfinancetracker.features.home.Timeframe

@Composable
fun TimeframeToggle(
    selectedTimeframe: Timeframe,
    onTimeframeSelected: (Timeframe) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusExtraLarge))
            .background(AppColorTokens.DarkSurface)
            .padding(AppTheme.dimensions.spacingExtraSmall),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TimeframeTab(
            text = stringResource(com.example.personalfinancetracker.R.string.home_timeframe_weekly),
            isSelected = selectedTimeframe == Timeframe.WEEKLY,
            onClick = { onTimeframeSelected(Timeframe.WEEKLY) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TimeframeTab(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) AppColorTokens.Gray800 else Color.Transparent
    val textColor = if (isSelected) AppColorTokens.White else AppColorTokens.Gray400

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusLarge))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = AppTheme.dimensions.spacingSmall),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = textColor
        )
    }
}
