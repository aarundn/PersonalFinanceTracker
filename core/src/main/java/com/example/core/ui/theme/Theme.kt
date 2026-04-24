package com.example.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val DarkColorScheme = darkColorScheme(
    primary = Gray50,
    secondary = Gray400,
    surfaceVariant = Gray800,
    surface = Gray900,
    onSurface = Gray950,
    tertiary = Gray50,
    onTertiary = Gray950,
    onPrimary = Gray950,
    background = Gray950,
    onBackground = Gray50,
)

private val LightColorScheme = lightColorScheme(
    primary = Gray50,
    secondary = Gray400,
    surfaceVariant = Gray800,
    surface = Gray900,
    onSurface = Gray950,
    tertiary = Gray50,
    onTertiary = Gray950,
    onPrimary = Gray950,
    background = Gray950,
    onBackground = Gray50,
)


@Composable
fun PersonalFinanceTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

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






