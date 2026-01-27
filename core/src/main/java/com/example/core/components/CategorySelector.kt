package com.example.core.components

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
    isIncome: Boolean,
    enabled: Boolean = true
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
        modifier = modifier,
        enabled = enabled
    )
}

@Preview(showBackground = true)
@Composable
private fun TransactionSelectorsPreview() {
    PersonalFinanceTrackerTheme {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            CategorySelector(
                selectedCategory = "Food",
                onCategorySelected = {},
                isIncome = false
            )

        }
    }
}



