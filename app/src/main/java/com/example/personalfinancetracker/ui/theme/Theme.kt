package com.example.personalfinancetracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    background = DarkBackground,
    onBackground = DarkForeground,
    primary = Primary,
    onPrimary = PrimaryForeground,
    secondary = Secondary,
    onSecondary = SecondaryForeground,
    surface = DarkCard,
    onSurface = DarkCardForeground,
    error = Destructive,
    onError = DestructiveForeground
)

private val LightColorScheme = lightColorScheme(
    background = Background,
    onBackground = Foreground,
    primary = Primary,
    onPrimary = PrimaryForeground,
    secondary = Secondary,
    onSecondary = SecondaryForeground,
    surface = Card,
    onSurface = CardForeground,
    error = Destructive,
    onError = DestructiveForeground
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}