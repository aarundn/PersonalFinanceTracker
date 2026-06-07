package com.example.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class AppExtendedColors(
    // Semantic Financial Colors
    val income: Color,
    val expense: Color,
    val warning: Color,

    // Category Colors
    val categoryFood: Color,
    val categoryTransport: Color,
    val categoryShopping: Color,
    val categoryBills: Color,
    val categoryEntertainment: Color,
    val categoryHealth: Color,
    val categoryEducation: Color,
    val categoryPersonal: Color,
    val categoryGroceries: Color,

    // Overlays & Containers
    val surfaceOverlay: Color,
    val successContainer: Color,
    val errorContainer: Color,
    val warningContainer: Color,

    // Semantic UI Chrome (derived from audit)
    val outlineDimmed: Color,
    val onSurfaceDimmed: Color,
)

fun darkExtendedColors() = AppExtendedColors(
    income  = AppColorTokens.Income,
    expense = AppColorTokens.Expense,
    warning = AppColorTokens.CoffeeYellow,

    categoryFood          = AppColorTokens.RestaurantsPink,
    categoryTransport     = AppColorTokens.TransportBlue,
    categoryShopping      = AppColorTokens.EntertainmentPurple,
    categoryBills         = AppColorTokens.HomeTeal,
    categoryEntertainment = AppColorTokens.EntertainmentPurple,
    categoryHealth        = AppColorTokens.Emerald500,
    categoryEducation     = AppColorTokens.Blue500,
    categoryPersonal      = AppColorTokens.GroceriesOrange,
    categoryGroceries     = AppColorTokens.GroceriesOrange,

    surfaceOverlay   = AppColorTokens.DarkSlate,
    successContainer = AppColorTokens.Income.withSubtleAlpha(),
    errorContainer   = AppColorTokens.Expense.withSubtleAlpha(),
    warningContainer = AppColorTokens.CoffeeYellow.withSubtleAlpha(),

    // outline.copy(alpha = 0.3f) → centralised here
    outlineDimmed   = Color(0xFF252525).copy(alpha = AppAlphaTokens.Disabled),
    // onSurface.copy(alpha = 0.6f) → centralised here
    onSurfaceDimmed = AppColorTokens.Gray50.copy(alpha = AppAlphaTokens.Scrim),
)

val LocalAppExtendedColors = staticCompositionLocalOf<AppExtendedColors> {
    error("No AppExtendedColors provided")
}
