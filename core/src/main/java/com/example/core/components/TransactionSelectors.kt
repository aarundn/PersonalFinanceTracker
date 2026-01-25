package com.example.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.model.Categories
import com.example.core.model.Categories.Companion.displayName
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.domain.model.Type

@Composable
fun CategorySelector(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    isIncome: Boolean
) {

    val currentType = if (isIncome) Type.INCOME else Type.EXPENSE

    val categories = remember(currentType) {
        Categories.forType(currentType)
    }


    TransactionDropdown(
        label = "Category",
        items = categories.map { it.name.displayName() },
        selectedItem = selectedCategory,
        onItemSelected = onCategorySelected,
        modifier = modifier
    )
}

val SUPPORTED_CURRENCIES = listOf(
    "DZD" to "DZD",
    "USD" to "$",
    "EUR" to "€",
    "GBP" to "£",
    "JPY" to "¥",
    "CAD" to "C$",
    "AUD" to "A$",
    "CHF" to "CHF",
    "CNY" to "¥",
    "INR" to "₹",
    "BRL" to "R$"
)

@Composable
fun CurrencySelector(
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit,
) {
    TransactionDropdown(
        label = "Currency",
        items = SUPPORTED_CURRENCIES.map { "${it.first} (${it.second})" },
        selectedItem = selectedCurrency.ifEmpty {
            "${SUPPORTED_CURRENCIES.first().first} (${SUPPORTED_CURRENCIES.first().second})"
        },
        onItemSelected = onCurrencySelected,
    )
}

@Preview(showBackground = true)
@Composable
private fun TransactionSelectorsPreview() {
    PersonalFinanceTrackerTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CategorySelector(
                selectedCategory = "Food",
                onCategorySelected = {},
                isIncome = false
            )

            CurrencySelector(
                selectedCurrency = "USD",
                onCurrencySelected = {}
            )
        }
    }
}



