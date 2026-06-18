package com.example.core.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Base color palette tokens derived directly from the Figma design.
 * Every color is fully opaque (alpha = 1.0). Apply transparency exclusively
 * via [AppAlphaTokens] constants and [Color.copy].
 */
object AppColorTokens {

    // ── Accent / Brand ──────────────────────────────────────────────
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
