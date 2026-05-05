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

)

fun darkExtendedColors() = AppExtendedColors(
    income = Green500,
    expense = Red500,
    warning = CoffeeYellow500,
    
    categoryFood = RestaurantsPink500,
    categoryTransport = TransportBlue500,
    categoryShopping = EntertainmentPurple500,
    categoryBills = HomeTeal500,
    categoryEntertainment = EntertainmentPurple500,
    categoryHealth = Color(0xFF10B981), // Defaulting to Teal for now
    categoryEducation = Color(0xFF3B82F6), // Defaulting to Blue for now
    categoryPersonal = Color(0xFFF97316), // Defaulting to Orange for now
    categoryGroceries = Color(0xFF6B7280), // Gray
    
    surfaceOverlay = DeepNavy,
    successContainer = Green500.copy(alpha = 0.12f),
    errorContainer = Red500.copy(alpha = 0.12f),
    warningContainer = CoffeeYellow500.copy(alpha = 0.12f),
)

val LocalAppExtendedColors = staticCompositionLocalOf<AppExtendedColors> {
    error("No AppExtendedColors provided")
}
