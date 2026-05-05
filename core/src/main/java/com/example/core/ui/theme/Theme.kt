package com.example.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

private val AppColorScheme = darkColorScheme(
    primary = Gray50,
    onPrimary = Gray950,
    primaryContainer = Gray800,
    onPrimaryContainer = Gray50,
    secondary = Gray400,
    onSecondary = Gray50,
    secondaryContainer = Miscellaneous,
    onSecondaryContainer = Gray50,
    tertiary = Gray50,
    onTertiary = Gray950,
    background = Gray950,
    onBackground = Gray50,
    surface = Gray900,
    onSurface = Gray50,
    surfaceVariant = Gray800,
    onSurfaceVariant = Gray400,
    surfaceContainer = Gray900,
    surfaceContainerHigh = Gray800,
    surfaceContainerHighest = Gray850,
    error = Red500,
    onError = Gray50,
    errorContainer = Red500.copy(alpha = 0.12f),
    onErrorContainer = Red500,
    outline = Gray500,
    outlineVariant = Gray800,
)

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

@Composable
fun PersonalFinanceTrackerTheme(
    content: @Composable () -> Unit
) {
    // Forced Dark Mode for now
    val colorScheme = AppColorScheme
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
