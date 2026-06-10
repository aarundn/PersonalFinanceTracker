package com.example.core.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Standardizes the alpha-based background and border styling for status-colored containers.
 * Used for financial summaries (Income/Expense/Balance cards) and system statuses (Sync banners).
 */
fun Modifier.statusContainer(
    baseColor: Color,
    cornerRadius: Dp = 12.dp,
    borderWidth: Dp = 1.dp,
): Modifier {
    val shape = RoundedCornerShape(cornerRadius)
    return this
        .clip(shape)
        .background(baseColor.withHoverAlpha())
        .border(borderWidth, baseColor.withDisabledAlpha(), shape)
}

@Composable
fun Modifier.cardsContainer(): Modifier {
    return this
        .background(
            brush = Brush.linearGradient(
                start = Offset.Infinite,
                end = Offset.Zero,
                colors = listOf(
                    MaterialTheme.colorScheme.surfaceContainerHighest.withHoverAlpha(),
                    MaterialTheme.colorScheme.surfaceContainerLow.withScrimAlpha()
                )
            ),
            shape = RoundedCornerShape(AppTheme.dimensions.radiusMedium)
        )
        .blur(
            radius = 10.dp,
            edgeTreatment = BlurredEdgeTreatment.Rectangle
        )
}
