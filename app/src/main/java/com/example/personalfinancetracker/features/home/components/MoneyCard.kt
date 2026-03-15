package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.ui.theme.statusContainer
import com.example.personalfinancetracker.features.budget.utils.formatCurrency

@Composable
fun MoneyCard(
    title: String,
    value: Double,
    modifier: Modifier = Modifier,
    currencySymbol: String = "$",
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    baseColor: Color = MaterialTheme.colorScheme.onSurface,
    textColor: Color = baseColor
) {
    Card(
        modifier = modifier.statusContainer(baseColor = baseColor, cornerRadius = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = null
    ) {
        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.Start) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.height(16.dp),
                        tint = textColor
                    )
                }
                Text(
                    title,
                    style = MaterialTheme.typography.labelSmall,
                    color = textColor
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = "${value.formatCurrency()} $currencySymbol",
                style = MaterialTheme.typography.headlineSmall,
                color = textColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MoneyCardPreview() {
    PersonalFinanceTrackerTheme {
        Column {
            MoneyCard(
                title = "Income",
                value = 5300.0,
                icon = Icons.Outlined.Check
            )
            MoneyCard(
                title = "Expenses",
                value = 2100.0,
                icon = Icons.Outlined.Clear
            )
            MoneyCard(
                title = "Balance",
                value = 3200.0,
                icon = Icons.Outlined.ShoppingCart
            )
        }
    }
}
