package com.example.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val AppColorScheme = darkColorScheme(
    primary = Gray50,
    onPrimary = Gray950,
    primaryContainer = Gray800,
    onPrimaryContainer = Gray50,
    secondary = Gray400,
    onSecondary = Gray50,
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
    error = ProgressError,
    onError = Gray50,
    errorContainer = ProgressError.copy(alpha = 0.12f),
    onErrorContainer = ProgressError,
    outline = Gray500,
    outlineVariant = Gray800,
)

@Composable
fun PersonalFinanceTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = AppColorScheme

    CompositionLocalProvider(
        LocalDimensions provides Dimensions()
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }
}






