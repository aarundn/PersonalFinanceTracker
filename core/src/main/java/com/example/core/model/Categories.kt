package com.example.core.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.example.core.R
import com.example.core.ui.theme.CategoryBills
import com.example.core.ui.theme.CategoryFood
import com.example.core.ui.theme.CategoryShopping
import com.example.core.ui.theme.CategoryTransport
import com.example.domain.model.Type

/**
 * Represents a Category UI model.
 * Note: Domain layer uses String for categories to allow future custom categories.
 * This class maps those Strings to UI resources.
 */
@Immutable
data class Category(
    val id: String,
    @StringRes val nameResId: Int,
    val icon: Int,
    val color: Color,
    val type: Type
)

object DefaultCategories {
    
    // Expense Categories
    val SHOPPING = Category("shopping", R.string.category_shopping, R.drawable.shopping_bag, CategoryShopping, Type.EXPENSE)
    val FOOD = Category("food", R.string.category_food, R.drawable.shopping_basket, CategoryFood, Type.EXPENSE)
    val TRANSPORT = Category("transport", R.string.category_transport, R.drawable.truck, CategoryTransport, Type.EXPENSE)
    val BILLS = Category("bills", R.string.category_bills, R.drawable.home, CategoryBills, Type.EXPENSE)
    val ENTERTAINMENT = Category("entertainment", R.string.category_entertainment, R.drawable.heart, CategoryFood, Type.EXPENSE)
    val HEALTH = Category("health", R.string.category_health, R.drawable.heart, CategoryBills, Type.EXPENSE)
    val OTHER = Category("other", R.string.category_other, R.drawable.box, Color.Gray, Type.EXPENSE)

    // Income Categories
    val SALARY = Category("salary", R.string.category_salary, R.drawable.wallet, CategoryBills, Type.INCOME)
    val FREELANCE = Category("freelance", R.string.category_freelance, R.drawable.briefcase, CategoryBills, Type.INCOME)
    val INVESTMENT = Category("investment", R.string.category_investment, R.drawable.trending_up, CategoryTransport, Type.INCOME)
    val GIFT = Category("gift", R.string.category_gift, R.drawable.heart, CategoryFood, Type.INCOME)
    val OTHER_INCOME = Category("other_income", R.string.category_other_income, R.drawable.box, Color.Gray, Type.INCOME)

    val all = listOf(
        SHOPPING, FOOD, TRANSPORT, BILLS, ENTERTAINMENT, HEALTH, OTHER,
        SALARY, FREELANCE, INVESTMENT, GIFT, OTHER_INCOME
    )

    fun getCategories(isIncome: Boolean): List<Category> {
        val targetType = if (isIncome) Type.INCOME else Type.EXPENSE
        return all.filter { it.type == targetType }
    }
    
    fun fromId(id: String): Category? = all.find { it.id == id }
}
