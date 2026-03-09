package com.example.core.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    bgAlpha: Float = 0.1f,
    borderAlpha: Float = 0.2f
): Modifier {
    val shape = RoundedCornerShape(cornerRadius)
    return this
        .clip(shape)
        .background(baseColor.copy(alpha = bgAlpha))
        .border(borderWidth, baseColor.copy(alpha = borderAlpha), shape)
}
