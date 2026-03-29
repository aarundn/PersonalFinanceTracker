package com.example.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.core.R
import com.example.core.ui.theme.Expense
import com.example.core.ui.theme.Income
import com.example.core.ui.theme.dimensions

@Composable
fun TransactionTypeToggle(
    isIncome: Boolean,
    onTypeChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(MaterialTheme.dimensions.radiusSmall)
            )
            .border(
                width = MaterialTheme.dimensions.borderThin,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                shape = RoundedCornerShape(MaterialTheme.dimensions.radiusSmall)
            )
            .padding(MaterialTheme.dimensions.spacingMedium)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.spacingMediumSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                modifier = Modifier.weight(1f),
                maxLines = 1,
                text = stringResource(R.string.transaction_type_label),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.spacingMediumSmall, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.transaction_type_expense),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (!isIncome) {
                        Expense
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    fontWeight = if (!isIncome) FontWeight.Medium else FontWeight.Normal
                )

                Switch(
                    checked = isIncome,
                    onCheckedChange = onTypeChanged
                )

                Text(
                    text = stringResource(R.string.transaction_type_income),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isIncome) {
                        Income // Green for income
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    fontWeight = if (isIncome) FontWeight.Medium else FontWeight.Normal
                )
            }
        }
    }
}