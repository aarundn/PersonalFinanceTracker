package com.example.personalfinancetracker.features.home.utils

import java.text.NumberFormat
import java.util.Locale

object TextFormattingUtils {
    
    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)
    private val numberFormatter = NumberFormat.getNumberInstance(Locale.US)
    
    /**
     * Format a double value as currency (e.g., $1,234.56)
     */
    fun formatCurrency(amount: Double): String {
        return currencyFormatter.format(amount)
    }
    
    /**
     * Format a double value as currency without decimals (e.g., $1,235)
     */
    fun formatCurrencyNoDecimals(amount: Double): String {
        return "$" + String.format("%.0f", amount)
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
    fun formatBudgetProgress(spent: Double, limit: Double): String {
        return "${formatCurrencyNoDecimals(spent)} of ${formatCurrencyNoDecimals(limit)}"
    }
    
}