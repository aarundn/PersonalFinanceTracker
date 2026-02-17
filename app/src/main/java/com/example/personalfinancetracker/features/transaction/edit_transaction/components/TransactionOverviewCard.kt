package com.example.personalfinancetracker.features.transaction.edit_transaction.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.R
import com.example.core.model.Category
import com.example.core.ui.theme.CategoryShopping
import com.example.core.ui.theme.Income
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.ui.theme.ProgressError
import com.example.core.utils.parseDateString
import com.example.core.model.DefaultCurrencies
import com.example.domain.model.Type
import com.example.personalfinancetracker.features.transaction.edit_transaction.EditTransactionState

@Composable
fun TransactionOverviewCard(
    state: EditTransactionState,
    modifier: Modifier = Modifier,
    category: Category
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            // Main content row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon with background
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .background(
                            color = category.color.copy(0.1f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(category.icon),
                        contentDescription = null,
                        tint = category.color,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Title and category (flex-1 equivalent)
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(category.nameResId),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(category.nameResId),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Amount and badge (text-right equivalent)
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    // Badge
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (state.isIncome)
                                category.color
                            else
                                ProgressError
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = if (state.isIncome) "Income" else "Expense",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // Amount
                    Text(
                        text = "${if (state.isIncome) "+" else "-"}${state.selectedCurrency?.symbol ?: "$"}${
                            String.format(
                                "%.2f",
                                state.amount.toDoubleOrNull() ?: 0.0
                            )
                        }",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (state.isIncome)
                            Income
                        else
                            ProgressError
                    )
                }
            }

            // Date and time row - Date at start, Time at end
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Date at start
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = parseDateString(state.date),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Time at end
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Home,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = parseDateString(state.date),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TransactionOverviewCardPreview() {
    PersonalFinanceTrackerTheme {
        TransactionOverviewCard(
            state = EditTransactionState(
                isIncome = false,
                selectedCategory = null,
                amount = "85.50",
                selectedCurrency = DefaultCurrencies.USD,
                date = "2024-03-15".toLong(),
                notes = "Weekly grocery shopping at Whole Foods",
                isEditing = false,

                ),
            category = Category(
                "shopping",
                R.string.category_shopping,
                R.drawable.shopping_bag,
                CategoryShopping,
                Type.EXPENSE
            )
        )
    }
}
