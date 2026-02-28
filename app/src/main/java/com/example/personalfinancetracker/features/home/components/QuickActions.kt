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
import androidx.compose.ui.unit.dp

import com.example.core.ui.theme.PersonalFinanceTrackerTheme

import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun QuickActions(
    onAddExpenseClick: () -> Unit,
    onAddIncomeClick: () -> Unit,
    onCurrencyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(bottom = 16.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Quick Actions", style = MaterialTheme.typography.titleMedium)
            
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ActionButton(
                    text = "Add Expense",
                    icon = Icons.Outlined.Add,
                    onClick = onAddExpenseClick,
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = "Add Income",
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
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.height(16.dp)
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
