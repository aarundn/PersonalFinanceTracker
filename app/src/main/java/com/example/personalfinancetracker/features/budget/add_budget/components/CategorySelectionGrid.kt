package com.example.personalfinancetracker.features.budget.add_budget.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.model.Category
import com.example.core.model.DefaultCategories
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.ui.theme.AppTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategorySelectionGrid(
    categories: List<Category>,
    selectedCategoryId: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Category",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = AppTheme.dimensions.spacingMediumSmall)
        )

        FlowRow(
            maxItemsInEachRow = 2,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacingMediumSmall),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacingMediumSmall)
        ) {
            categories.forEach { category ->
                val isSelected = category.id == selectedCategoryId
                Surface(
                    shape = RoundedCornerShape(AppTheme.dimensions.radiusMedium),
                    color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface,
                    tonalElevation = if (isSelected) 2.dp else 0.dp,
                    modifier = Modifier
                        .weight(1f, fill = true)
                        .clip(RoundedCornerShape(AppTheme.dimensions.radiusMedium))
                        .border(
                            width = AppTheme.dimensions.borderThin,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(AppTheme.dimensions.radiusMedium)
                        )
                        .clickable { onCategorySelected(category.id) }
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = AppTheme.dimensions.spacingMedium, vertical = AppTheme.dimensions.spacingMediumSmall),
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacingMediumSmall),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconWrapper(
                            background = category.color.copy(alpha = 0.12f),
                            tint = category.color
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(category.icon),
                                contentDescription = null,
                                tint = category.color,
                                modifier = Modifier.size(AppTheme.dimensions.iconSizeNormal)
                            )
                        }

                        Column(
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = stringResource(category.nameResId),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isSelected) "Selected" else "Tap to select",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
private fun IconWrapper(
    background: Color,
    tint: Color,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(AppTheme.dimensions.iconSizeMediumLarge)
            .clip(CircleShape)
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun CategorySelectionGridPreview() {
    PersonalFinanceTrackerTheme {
        CategorySelectionGrid(
            categories = listOf(
                DefaultCategories.FOOD,
                DefaultCategories.TRANSPORT
            ),
            selectedCategoryId = "",
            onCategorySelected = {}
        )
    }
}