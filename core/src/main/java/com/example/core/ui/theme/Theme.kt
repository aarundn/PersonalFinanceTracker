package com.example.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

// =============================================================================
// Material 3 Color Schemes — built entirely from AppColorTokens + AppAlphaTokens
// =============================================================================

private val DarkColorScheme = darkColorScheme(
    primary            = AppColorTokens.Gray50,
    onPrimary          = AppColorTokens.Black,
    primaryContainer   = AppColorTokens.Gray800,
    onPrimaryContainer = AppColorTokens.Gray50,

    secondary            = AppColorTokens.Gray400,
    onSecondary          = AppColorTokens.Gray50,
    secondaryContainer   = AppColorTokens.Gray800,
    onSecondaryContainer = AppColorTokens.Gray50,

    tertiary    = AppColorTokens.Gray50,
    onTertiary  = AppColorTokens.Black,

    background   = AppColorTokens.Black,
    onBackground = AppColorTokens.Gray50,

    surface                 = Color(0xFF18181B),  // Gray900 — kept for surface continuity
    onSurface               = AppColorTokens.Gray50,
    surfaceVariant          = Color(0xFF27272A),   // Gray800 — kept for variant continuity
    onSurfaceVariant        = AppColorTokens.Gray400,
    surfaceContainer        = Color(0xFF18181B),
    surfaceContainerHigh    = Color(0xFF27272A),
    surfaceContainerHighest = AppColorTokens.DarkSurface,

    error            = AppColorTokens.Expense,
    onError          = AppColorTokens.Gray50,
    errorContainer   = AppColorTokens.Expense.withSubtleAlpha(),
    onErrorContainer = AppColorTokens.Expense,

    outline        = Color(0xFF252525),  // kept: original outline shade
    outlineVariant = Color(0xFF27272A),
)

private val LightColorScheme = lightColorScheme(
    primary            = AppColorTokens.Blue500,
    onPrimary          = AppColorTokens.White,
    primaryContainer   = AppColorTokens.Blue500.withLightAlpha(),
    onPrimaryContainer = AppColorTokens.Blue500,

    secondary            = AppColorTokens.Gray600,
    onSecondary          = AppColorTokens.White,
    secondaryContainer   = AppColorTokens.Gray400,
    onSecondaryContainer = AppColorTokens.Black,

    tertiary    = AppColorTokens.Violet500,
    onTertiary  = AppColorTokens.White,

    background   = AppColorTokens.White,
    onBackground = AppColorTokens.Black,

    surface          = AppColorTokens.Gray50,
    onSurface        = AppColorTokens.Black,
    surfaceVariant   = AppColorTokens.Gray50,
    onSurfaceVariant = AppColorTokens.Gray600,

    error            = AppColorTokens.Expense,
    onError          = AppColorTokens.White,
    errorContainer   = AppColorTokens.Expense.withSubtleAlpha(),
    onErrorContainer = AppColorTokens.Expense,

    outline        = AppColorTokens.Gray400,
    outlineVariant = AppColorTokens.Gray50,
)

// =============================================================================
// AppTheme — accessor for extended custom design tokens
// =============================================================================

object AppTheme {
    val colors: AppExtendedColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppExtendedColors.current

    val dimensions: Dimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalDimensions.current
}

// =============================================================================
// Root composable theme wrapper
// =============================================================================

@Composable
fun PersonalFinanceTrackerTheme(
    content: @Composable () -> Unit
) {
    // Forced Dark Mode for now
    val colorScheme = DarkColorScheme
    val extendedColors = darkExtendedColors()

    CompositionLocalProvider(
        LocalDimensions provides Dimensions(),
        LocalAppExtendedColors provides extendedColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }
}
