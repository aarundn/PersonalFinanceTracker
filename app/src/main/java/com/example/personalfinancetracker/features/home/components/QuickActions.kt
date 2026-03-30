package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.ui.theme.dimensions
import com.example.core.R
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

@Composable
fun QuickActions(
    onAddExpenseClick: () -> Unit,
    onAddIncomeClick: () -> Unit,
    onCurrencyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(bottom = MaterialTheme.dimensions.spacingMedium),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = BorderStroke(MaterialTheme.dimensions.borderThin, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Column(Modifier.padding(MaterialTheme.dimensions.spacingMedium), verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.spacingMediumSmall)) {
            Text(stringResource(R.string.home_quick_actions), style = MaterialTheme.typography.titleMedium)
            
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.spacingMediumSmall)) {
                ActionButton(
                    text = stringResource(R.string.home_add_expense),
                    icon = Icons.Outlined.Add,
                    onClick = onAddExpenseClick,
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = stringResource(R.string.home_add_income),
                    icon = Icons.Outlined.Add,
                    onClick = onAddIncomeClick,
                    modifier = Modifier.weight(1f)
                )
            }
            ActionButton(
                text = "Currency & Exchange Rates",
                icon = Icons.Outlined.Add,
                onClick = onCurrencyClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(MaterialTheme.dimensions.borderThin, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.spacingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.height(MaterialTheme.dimensions.iconSizeSmall)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun QuickActionsPreview() {
    PersonalFinanceTrackerTheme {
        QuickActions(
            onAddExpenseClick = {},
            onAddIncomeClick = {},
            onCurrencyClick = {}
        )
    }
}
