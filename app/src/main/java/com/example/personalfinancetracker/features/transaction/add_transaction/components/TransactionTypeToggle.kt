package com.example.personalfinancetracker.features.transaction.add_transaction.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.personalfinancetracker.ui.theme.Expense
import com.example.personalfinancetracker.ui.theme.Income
import com.example.personalfinancetracker.ui.theme.PersonalFinanceTrackerTheme

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
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Transaction\nType",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Expense",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (!isIncome) {
                        Expense // Red for expense
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
                    text = "Income",
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

@Preview(showBackground = true)
@Composable
private fun TransactionTypeTogglePreview() {
    PersonalFinanceTrackerTheme {
        TransactionTypeToggle(
            isIncome = false,
            onTypeChanged = {}
        )
    }
}
