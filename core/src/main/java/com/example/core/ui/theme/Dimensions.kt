package com.example.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Standardized dimensions replacing hardcoded dp strings everywhere in the app.
 * Adheres to Material 3 8dp/4dp baseline grid principles.
 */
data class Dimensions(
    // Spacing & Padding
    val spacingExtraSmall: Dp = 4.dp,
    val spacingSmall: Dp = 8.dp,
    val spacingMediumSmall: Dp = 12.dp,
    val spacingMedium: Dp = 16.dp,
    val spacingLarge: Dp = 24.dp,
    val spacingExtraLarge: Dp = 32.dp,

    // Borders & Strokes
    val borderThin: Dp = 1.dp,
    val borderNormal: Dp = 2.dp,
    val borderThick: Dp = 4.dp,

    // Component Specific Heights
    val buttonHeightSmall: Dp = 36.dp,
    val buttonHeightNormal: Dp = 48.dp,
    val buttonHeightLarge: Dp = 56.dp,
    val inputHeightNormal: Dp = 56.dp,

    // Icon Sizes
    val iconSizeSmall: Dp = 16.dp,
    val iconSizeNormal: Dp = 24.dp,
    val iconSizeLarge: Dp = 32.dp,
    val iconSizeMediumLarge: Dp = 40.dp,
    val iconSizeExtraLarge: Dp = 48.dp,
    val iconSizeHuge: Dp = 64.dp,
    
    // Optional Radius Fallbacks (Although MaterialTheme.shapes is preferred)
    val radiusSmall: Dp = 8.dp,
    val radiusMedium: Dp = 12.dp,
    val radiusLarge: Dp = 16.dp,
    val radiusExtraLarge: Dp = 24.dp,
)

val LocalDimensions = staticCompositionLocalOf { Dimensions() }

/**
 * Extension property wrapper allowing access via MaterialTheme.dimensions
 */
val MaterialTheme.dimensions: Dimensions
    @Composable
    @ReadOnlyComposable
    get() = LocalDimensions.current
