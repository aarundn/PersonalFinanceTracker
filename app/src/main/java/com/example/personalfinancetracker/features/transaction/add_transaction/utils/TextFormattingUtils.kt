package com.example.personalfinancetracker.features.transaction.add_transaction.utils

import java.text.NumberFormat
import java.util.Locale

object TextFormattingUtils {

    private val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 2
    }

    fun formatCurrency(amount: Double): String = currencyFormat.format(amount)
}

