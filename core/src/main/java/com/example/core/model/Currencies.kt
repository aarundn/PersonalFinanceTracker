package com.example.core.model

import androidx.compose.runtime.Immutable

/**
 * Represents a Currency UI model.
 * Note: Domain layer uses String (currency ID) to allow flexibility.
 * This class maps those Strings to UI-friendly properties.
 */
@Immutable
data class Currency(
    val id: String,
    val name: String,
    val symbol: String
)

object DefaultCurrencies {

    val DZD = Currency("DZD", "Algerian Dinar", "DZD")
    val USD = Currency("USD", "US Dollar", "$")
    val EUR = Currency("EUR", "Euro", "€")
    val GBP = Currency("GBP", "British Pound", "£")
    val JPY = Currency("JPY", "Japanese Yen", "¥")
    val CAD = Currency("CAD", "Canadian Dollar", "C$")
    val AUD = Currency("AUD", "Australian Dollar", "A$")
    val CHF = Currency("CHF", "Swiss Franc", "CHF")
    val CNY = Currency("CNY", "Chinese Yuan", "¥")
    val INR = Currency("INR", "Indian Rupee", "₹")
    val BRL = Currency("BRL", "Brazilian Real", "R$")

    val all = listOf(DZD, USD, EUR, GBP, JPY, CAD, AUD, CHF, CNY, INR, BRL)

    fun fromId(id: String): Currency? = all.find { it.id == id }
}
