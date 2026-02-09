package com.example.core.model

import androidx.compose.ui.graphics.Color
import com.example.core.R
import com.example.core.ui.theme.CategoryBills
import com.example.core.ui.theme.CategoryFood
import com.example.core.ui.theme.CategoryShopping
import com.example.core.ui.theme.CategoryTransport
import com.example.domain.model.Type

enum class Categories(val icon: Int, val color: Color, val Type: Type) {
    // Expense
    SHOPPING(R.drawable.shopping_bag, CategoryShopping, Type.EXPENSE),
    FOOD(R.drawable.shopping_basket, CategoryFood, Type.EXPENSE),
    TRANSPORT(R.drawable.truck, CategoryTransport, Type.EXPENSE),
    BILLS(R.drawable.home, CategoryBills, Type.EXPENSE),
    ENTERTAINMENT(R.drawable.heart, CategoryFood, Type.EXPENSE),
    HEALTH(R.drawable.heart, CategoryBills, Type.EXPENSE),
    OTHER(R.drawable.box, Color.Gray, Type.EXPENSE),

    // Income
    SALARY(R.drawable.wallet, CategoryBills, Type.INCOME),
    FREELANCE(R.drawable.briefcase, CategoryBills, Type.INCOME),
    INVESTMENT(R.drawable.trending_up, CategoryTransport, Type.INCOME),
    GIFT(R.drawable.heart, CategoryFood, Type.INCOME),
    OTHER_INCOME(R.drawable.box, Color.Gray, Type.INCOME);

    companion object {
        fun String.displayName(): String =
            this.lowercase().replaceFirstChar { it.uppercase() }
                .replace("_", " ")

        fun forType(Type: Type): List<Categories> =
            entries.filter { it.Type == Type }

        fun getCategories(isIncome: Boolean): List<Categories> {
            val type = if (isIncome) Type.INCOME else Type.EXPENSE
            return forType(type)
        }

        fun fromName(name: String): Categories? =
             entries.find { it.name.equals(name, ignoreCase = true) }
    }
}

