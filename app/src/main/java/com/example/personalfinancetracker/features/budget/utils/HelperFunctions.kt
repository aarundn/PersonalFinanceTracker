package com.example.personalfinancetracker.features.budget.utils

import android.annotation.SuppressLint
import kotlin.math.max

fun calculateDaysElapsed(createdAt: Long, daysInPeriod: Int): Int {
    val diff = System.currentTimeMillis() - createdAt
    val days = (diff / (1000 * 60 * 60 * 24)).toInt()
    return max(0, minOf(days, daysInPeriod))
}

@SuppressLint("DefaultLocale")
fun Double.formatCurrency(): String = String.format("%,.2f", this)