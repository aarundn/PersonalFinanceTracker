package com.example.core.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Base color palette tokens derived directly from the Figma design.
 * Every color is fully opaque (alpha = 1.0). Apply transparency exclusively
 * via [AppAlphaTokens] constants and [Color.copy].
 */
object AppColorTokens {

    // ── Accent / Brand ───────────────────────────────────────────────
    val Blue500      = Color(0xFF3B82F6)
    val Violet500    = Color(0xFF8B5CF6)
    val Pink500      = Color(0xFFEC4899)
    val Emerald500   = Color(0xFF10B981)

    // ── Neutrals ─────────────────────────────────────────────────────
    val White        = Color(0xFFFFFFFF)
    val Black        = Color(0xFF000000)
    val AlmostBlack  = Color(0xFF050505)

    // ── Surfaces & Backgrounds ───────────────────────────────────────
    val DarkBackground = Color(0xFF030213)
    val DarkSurface    = Color(0xFF121212)
    val DarkSlate      = Color(0xFF0C1A31)

    // ── Grays (UI chrome: text, borders, tracks) ─────────────────────
    val Gray50       = Color(0xFFFAFAFA)
    val Gray400      = Color(0xFF9CA3AF)
    val Gray500      = Color(0xFF6B7280)
    val Gray600      = Color(0xFF4B5563)
    val Gray800      = Color(0xFF1F2937)

    // ── Semantic Financial ────────────────────────────────────────────
    val Income       = Emerald500
    val Expense      = Color(0xFFE11D48)

    // ── Category Accents ─────────────────────────────────────────────
    val GroceriesOrange  = Color(0xFFF97316)
    val TransportBlue    = Blue500
    val RestaurantsPink  = Pink500
    val HomeTeal         = Emerald500
    val EntertainmentPurple = Color(0xFFA855F7)
    val FreelanceCyan    = Color(0xFF2DD4BF)
    val CoffeeYellow     = Color(0xFFF59E0B)
}

/**
 * Standardised alpha (opacity) tokens.
 * Use these instead of raw float literals to keep all transparency
 * values consistent and traceable back to the Figma design.
 */
object AppAlphaTokens {
    /** Fully transparent — e.g. invisible overlay base state. */
    const val Transparent: Float = 0.00f

    /** Subtle tint — e.g. container backgrounds for status cards. */
    const val Subtle: Float = 0.08f

    /** Hover / pressed state overlay. */
    const val Hover: Float = 0.10f

    /** Light emphasis — e.g. selected chip backgrounds. */
    const val Light: Float = 0.15f

    /** Disabled state foreground / background. */
    const val Disabled: Float = 0.25f

    /** Medium scrim — e.g. semi-transparent bottom-sheet backdrops. */
    const val Scrim: Float = 0.50f
}

// =============================================================================
// Semantic Alpha Extensions
// Convenience helpers that combine a Color with an AppAlphaToken.
// =============================================================================

/** Returns this color with [AppAlphaTokens.Hover] opacity. */
fun Color.withHoverAlpha(): Color = copy(alpha = AppAlphaTokens.Hover)

/** Returns this color with [AppAlphaTokens.Light] opacity. */
fun Color.withLightAlpha(): Color = copy(alpha = AppAlphaTokens.Light)

/** Returns this color with [AppAlphaTokens.Disabled] opacity. */
fun Color.withDisabledAlpha(): Color = copy(alpha = AppAlphaTokens.Disabled)

/** Returns this color with [AppAlphaTokens.Scrim] opacity. */
fun Color.withScrimAlpha(): Color = copy(alpha = AppAlphaTokens.Scrim)

/** Returns this color with [AppAlphaTokens.Subtle] opacity — status containers. */
fun Color.withSubtleAlpha(): Color = copy(alpha = AppAlphaTokens.Subtle)

// =============================================================================
// Legacy aliases — keep for binary compatibility during migration.
// Prefer AppColorTokens.XYZ in new code.
// =============================================================================

@Deprecated("Use AppColorTokens.Gray50", ReplaceWith("AppColorTokens.Gray50"))
val Gray50 = AppColorTokens.Gray50

@Deprecated("Use AppColorTokens.Gray400", ReplaceWith("AppColorTokens.Gray400"))
val Gray400 = AppColorTokens.Gray400

@Deprecated("Use AppColorTokens.Gray800", ReplaceWith("AppColorTokens.Gray800"))
val Miscellaneous = AppColorTokens.Gray800

@Deprecated("Use AppColorTokens.Gray500", ReplaceWith("AppColorTokens.Gray500"))
val Gray500 = Color(0xFF252525)  // kept as-is: originally a different shade used for outlines

@Deprecated("Use AppColorTokens.DarkSurface", ReplaceWith("AppColorTokens.DarkSurface"))
val Gray800 = Color(0xFF27272A)  // kept as-is: slightly different neutral used for surface variant

@Deprecated("Use AppColorTokens.DarkSurface", ReplaceWith("AppColorTokens.DarkSurface"))
val Gray850 = AppColorTokens.DarkSurface

@Deprecated("Use MaterialTheme.colorScheme.surface", ReplaceWith("MaterialTheme.colorScheme.surface"))
val Gray900 = Color(0xFF18181B)

@Deprecated("Use AppColorTokens.Black", ReplaceWith("AppColorTokens.Black"))
val Gray950 = AppColorTokens.Black

@Deprecated("Use AppColorTokens.Income", ReplaceWith("AppColorTokens.Income"))
val Green500 = AppColorTokens.Income

@Deprecated("Use AppColorTokens.Expense", ReplaceWith("AppColorTokens.Expense"))
val Red500 = AppColorTokens.Expense

@Deprecated("Use AppColorTokens.DarkSlate", ReplaceWith("AppColorTokens.DarkSlate"))
val DeepNavy = AppColorTokens.DarkSlate

@Deprecated("Use AppColorTokens.GroceriesOrange", ReplaceWith("AppColorTokens.GroceriesOrange"))
val GroceriesOrange500 = AppColorTokens.GroceriesOrange

@Deprecated("Use AppColorTokens.TransportBlue", ReplaceWith("AppColorTokens.TransportBlue"))
val TransportBlue500 = AppColorTokens.TransportBlue

@Deprecated("Use AppColorTokens.RestaurantsPink", ReplaceWith("AppColorTokens.RestaurantsPink"))
val RestaurantsPink500 = AppColorTokens.RestaurantsPink

@Deprecated("Use AppColorTokens.HomeTeal", ReplaceWith("AppColorTokens.HomeTeal"))
val HomeTeal500 = AppColorTokens.HomeTeal

@Deprecated("Use AppColorTokens.EntertainmentPurple", ReplaceWith("AppColorTokens.EntertainmentPurple"))
val EntertainmentPurple500 = AppColorTokens.EntertainmentPurple

@Deprecated("Use AppColorTokens.FreelanceCyan", ReplaceWith("AppColorTokens.FreelanceCyan"))
val FreelanceCyan700 = AppColorTokens.FreelanceCyan

@Deprecated("Use AppColorTokens.CoffeeYellow", ReplaceWith("AppColorTokens.CoffeeYellow"))
val CoffeeYellow500 = AppColorTokens.CoffeeYellow
