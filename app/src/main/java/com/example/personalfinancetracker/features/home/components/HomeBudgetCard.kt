package com.example.personalfinancetracker.features.home.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import com.example.core.components.IconWrapper
import com.example.core.ui.theme.AppTheme
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.ui.theme.cardsContainer
import com.example.core.utils.formatPercentage
import com.example.personalfinancetracker.features.budget.model.BudgetUi

@Composable
fun HomeBudgetCard(
    modifier: Modifier = Modifier,
    budget: BudgetUi,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .cardsContainer(AppTheme.dimensions.radiusExtraLarge)

        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(AppTheme.dimensions.radiusExtraLarge),
            border = BorderStroke(
                AppTheme.dimensions.borderThin,
                MaterialTheme.colorScheme.outline
            ),
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = AppTheme.dimensions.spacingMedium)
                    .padding(vertical = AppTheme.dimensions.spacingMedium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacingMediumSmall)
            ) {
                Box(
                    modifier = Modifier.clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    CustomCircularProgressBar(
                        progress = budget.percentage,
                        modifier = Modifier,
                        progressColor = budget.currentCategory.color,
                    )
                    IconWrapper(
                        modifier = Modifier
                            .size(55.dp)
                            .clip(CircleShape),
                        borderColor = Color.Transparent,
                        icon = ImageVector.vectorResource(budget.currentCategory.icon),
                        iconColor = budget.currentCategory.color
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacingExtraSmall)
                ) {
                    Text(
                        text = stringResource(budget.currentCategory.nameResId),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = formatPercentage(budget.percentage),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
private fun HomeBudgetCardPreview() {
    PersonalFinanceTrackerTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            HomeBudgetCard(
                budget = BudgetUi(
                    id = "1",
                    userId = "user_1",
                    category = "food",
                    amount = 500.0,
                    spent = 300.0,
                    currency = "USD",
                    period = "monthly",
                    notes = null,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }
}
