package com.example.core.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color

data class BudgetDisplayData(
    val iconTint: Color,
    val iconBackground: Color,
    @DrawableRes val icon: Int,
    @StringRes val categoryName: Int,
    val currencySymbol: String,
    val spent: Double,
    val amount: Double,
    val isOverBudget: Boolean,
    val isWarning: Boolean,
    val overBudget: Double,
    val remaining: Double
)
