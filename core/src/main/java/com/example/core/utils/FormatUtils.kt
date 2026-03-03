package com.example.core.utils

import java.util.Locale

/**
 * Format string e.g., "$ 385 of $ 500"
 */
fun formatBudgetSpentOfAmount(currencySymbol: String, spent: String, amount: String): String {
    return "$currencySymbol $spent of $currencySymbol $amount"
}

/**
 * Format string e.g., "$ 15 over"
 */
fun formatAmountOver(currencySymbol: String, amount: String): String {
    return "$currencySymbol $amount over"
}

/**
 * Format string e.g., "$ 115 left"
 */
fun formatAmountLeft(currencySymbol: String, amount: String): String {
    return "$currencySymbol $amount left"
}

/**
 * Format string e.g., "$385"
 */
fun formatCurrencyAmount(symbol: String, amount: Double, decimals: Int = 0): String {
    return "$symbol${String.format(Locale.getDefault(), "%.${decimals}f", amount)}"
}

/**
 * Format string e.g., "77.0% used"
 */
fun formatPercentageUsed(progress: Float): String {
    return "${String.format(Locale.getDefault(), "%.1f", progress * 100)}% used"
}

/**
 * Format string e.g., "$115 remaining"
 */
fun formatAmountRemaining(symbol: String, amount: Double): String {
    return "$symbol${String.format(Locale.getDefault(), "%.0f", amount)} remaining"
}

/**
 * Format string e.g., "$ 500.00"
 */
fun formatAmount(currencySymbol: String, amount: String): String {
    return "$currencySymbol $amount"
}

/**
 * Format string e.g., "$500.00" (no space)
 */
fun formatAmountNoSpace(currencySymbol: String, amount: String): String {
    return "$currencySymbol$amount"
}

/**
 * Format string e.g., "77.0%"
 */
fun formatPercentage(progress: Float): String {
    return "${String.format(Locale.getDefault(), "%.1f", progress * 100)}%"
}
