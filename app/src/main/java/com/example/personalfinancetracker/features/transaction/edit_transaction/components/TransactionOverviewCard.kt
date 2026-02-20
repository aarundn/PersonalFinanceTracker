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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.R
import com.example.core.model.DefaultCurrencies
import com.example.core.ui.theme.Income
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.ui.theme.ProgressError
import com.example.domain.model.Type
import com.example.personalfinancetracker.features.budget.utils.formatCurrency
import com.example.personalfinancetracker.features.transaction.model.TransactionUi

@Composable
fun TransactionOverviewCard(
    transactionUi: TransactionUi,
    modifier: Modifier = Modifier,
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

            CategoryAndAmountInfo(
                isIncome = transactionUi.isIncome,
                categoryColor = transactionUi.currentCategory.color,
                categoryName = stringResource(transactionUi.currentCategory.nameResId),
                currencySymbol = transactionUi.currencySymbol,
                amount = transactionUi.amount,
                categoryIconId = transactionUi.currentCategory.icon
            )

            TimeAndDate(
                date = transactionUi.formattedDate,
                time = transactionUi.formattedTime
            )
        }
    }
}

@Composable
private fun TimeAndDate(date: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        TimeLabel(
            date,
            R.drawable.calendar
        )

        TimeLabel(
            time,
            R.drawable.clock
        )
    }
}

@Composable
private fun TimeLabel(textLabel: String, icon: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = textLabel,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun CategoryAndAmountInfo(
    isIncome: Boolean,
    categoryColor: Color,
    categoryName: String,
    currencySymbol: String,
    amount: Double,
    categoryIconId: Int,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(58.dp)
                .background(
                    color = categoryColor.copy(0.1f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(categoryIconId),
                contentDescription = null,
                tint = categoryColor,
                modifier = Modifier.size(32.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = categoryName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = categoryName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        AmountWithBadge(
            modifier = Modifier.weight(1f),
            isIncome = isIncome,
            categoryColor = categoryColor,
            currencySymbol = currencySymbol,
            amount = amount
        )
    }
}

@Composable
private fun AmountWithBadge(
    isIncome: Boolean,
    categoryColor: Color,
    currencySymbol: String,
    amount: Double,

    modifier: Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isIncome)
                    categoryColor
                else
                    ProgressError
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = if (isIncome) "Income" else "Expense",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        Text(
            text = "${if (isIncome) "+" else "-"}${currencySymbol}${
                amount.formatCurrency()
            }",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = if (isIncome)
                Income
            else
                ProgressError
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TransactionOverviewCardPreview() {
    PersonalFinanceTrackerTheme {
        TransactionOverviewCard(
            transactionUi = TransactionUi(
                id = "",
                userId = "",
                amount = 100.0,
                currency = DefaultCurrencies.USD.id,
                categoryId = "",
                date = System.currentTimeMillis(),
                notes = "Weekly grocery shopping at Whole Foods",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                type = Type.EXPENSE
            )
        )
    }
}
