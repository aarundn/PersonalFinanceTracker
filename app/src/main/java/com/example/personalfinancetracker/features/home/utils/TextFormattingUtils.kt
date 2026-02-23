package com.example.personalfinancetracker.features.home.utils

import java.text.NumberFormat
import java.util.Locale

object TextFormattingUtils {

    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)
    private val numberFormatter = NumberFormat.getNumberInstance(Locale.US)

    /**
     * Format a double value as currency (e.g., $1,234.56) using the provided symbol.
     */
    fun formatCurrency(amount: Double, symbol: String = "$"): String {
        return "$symbol${numberFormatter.format(amount)}"
    }

    /**
     * Format a double value as currency without decimals (e.g., $1,235) using the provided symbol.
     */
    fun formatCurrencyNoDecimals(amount: Double, symbol: String = "$"): String {
        return "$symbol${String.format("%.0f", amount)}"
    }

    /**
     * Format a double value as a number with 2 decimal places (e.g., 1,234.56)
     */
    fun formatNumber(amount: Double): String {
        return String.format("%.2f", amount)
    }

    /**
     * Format a double value as a number without decimals (e.g., 1,235)
     */
    fun formatNumberNoDecimals(amount: Double): String {
        return String.format("%.0f", amount)
    }

    /**
     * Format a double value as percentage with 1 decimal place (e.g., 63.3%)
     */
    fun formatPercentage(value: Double): String {
        return String.format("%.1f%%", value * 100)
    }

    /**
     * Format budget progress text (e.g., "$385 of $500")
     */
    fun formatBudgetProgress(spent: Double, limit: Double, symbol: String = "$"): String {
        return "${formatCurrencyNoDecimals(spent, symbol)} of ${formatCurrencyNoDecimals(limit, symbol)}"
    }

    /**
     * Format savings progress text (e.g., "$950 of $1,500")
     */
    fun formatSavingsProgress(saved: Double, goal: Double, symbol: String = "$"): String {
        return "${formatCurrencyNoDecimals(saved, symbol)} of ${formatCurrencyNoDecimals(goal, symbol)} goal"
    }

    /**
     * Format remaining amount text (e.g., "$550 remaining to reach your goal")
     */
    fun formatRemainingAmount(remaining: Double, symbol: String = "$"): String {
        return "${formatCurrencyNoDecimals(remaining, symbol)} remaining to reach your goal"
    }
}