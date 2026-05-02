package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.ui.theme.dimensions
import com.example.core.utils.formatPercentage
import com.example.personalfinancetracker.features.budget.model.BudgetUi

@Composable
fun HomeBudgetCard(
    modifier: Modifier = Modifier,
    budget: BudgetUi,
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(MaterialTheme.dimensions.radiusMedium),
        border = BorderStroke(
            MaterialTheme.dimensions.borderThin,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.dimensions.spacingMedium)
                .padding(vertical = MaterialTheme.dimensions.spacingSmall),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.spacingMediumSmall)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(budget.currentCategory.icon),
                    contentDescription = null,
                    tint = budget.currentCategory.color
                )
                CustomCircularProgressBar(
                    progress = budget.percentage,
                    modifier = Modifier,
                    progressColor = budget.currentCategory.color,
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.spacingExtraSmall)
            ) {
                Text(
                    text = stringResource(budget.currentCategory.nameResId),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatPercentage(budget.percentage),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeBudgePreview() {
    PersonalFinanceTrackerTheme {
        HomeBudgetCard(
            budget = BudgetUi(
                id = "1",
                category = "food",
                amount = 500.0,
                spent = 300.0,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                period = "monthly",
                notes = "",
                currency = "USD",
                userId = "",
                syncStatusEnum = "PENDING"
            )
        )
    }
}