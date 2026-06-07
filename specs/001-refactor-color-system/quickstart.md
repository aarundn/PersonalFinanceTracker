# Quickstart: Using the New Color System

This guide explains how to use the newly refactored Compose color system based on Figma tokens.

## 1. Using Standard Material Colors
The base theme is built on standard Material 3 color roles.
```kotlin
// Example: Using the primary color (Blue500)
Box(modifier = Modifier.background(MaterialTheme.colorScheme.primary))
```

## 2. Using Alpha Variants (The "Pro" Way)
Instead of using hardcoded transparent hex values, you must use the standard alpha tokens provided by the system. This ensures consistency and makes future updates easier.

```kotlin
// Import the Alpha Tokens
import com.aarundn.personalfinancetracker.core.presentation.theme.AppAlphaTokens

// Correct way to apply transparency:
Box(
    modifier = Modifier.background(
        MaterialTheme.colorScheme.primary.copy(alpha = AppAlphaTokens.Hover)
    )
)
```

## 3. What NOT to Do
Never hardcode hex values or use raw floats for alphas unless absolutely necessary for a one-off animation.
```kotlin
// ❌ WRONG: Hardcoded alpha hex
val BadColor = Color(0x263B82F6)

// ❌ WRONG: Raw float value
Box(modifier = Modifier.background(Color.Blue.copy(alpha = 0.15f)))

// ✅ RIGHT: Use tokens
val GoodColor = AppColorTokens.Blue500.copy(alpha = AppAlphaTokens.Hover)
```
