package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.core.R
import com.example.core.components.CustomProgressBar
import com.example.personalfinancetracker.features.budget.utils.formatCurrency
import com.example.core.ui.theme.dimensions

@Composable
fun MonthCard(
    totalTransactions: Int,
    dailyAverage: Double,
    daysPassed: Int,
    daysInMonth: Int,
    currencySymbol: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        border = BorderStroke(MaterialTheme.dimensions.borderThin, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Column(Modifier.padding(MaterialTheme.dimensions.spacingMedium)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.spacingSmall)
            ) {
                Icon(ImageVector.vectorResource(R.drawable.calendar), contentDescription = null)
                Text(stringResource(R.string.home_this_month), style = MaterialTheme.typography.titleMedium)
            }
            Spacer(Modifier.height(MaterialTheme.dimensions.spacingMedium))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        stringResource(R.string.home_total_transactions),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(MaterialTheme.dimensions.spacingExtraSmall))
                    Text(
                        totalTransactions.toString(),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        stringResource(R.string.home_daily_average),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(MaterialTheme.dimensions.spacingExtraSmall))
                    Text(
                        "${dailyAverage.formatCurrency()} $currencySymbol",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
            Spacer(Modifier.height(MaterialTheme.dimensions.spacingMedium))
            Column {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        stringResource(R.string.home_days_passed),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "$daysPassed of $daysInMonth",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.height(MaterialTheme.dimensions.spacingSmall))
                CustomProgressBar(
                    progress = daysPassed / daysInMonth.toFloat(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
