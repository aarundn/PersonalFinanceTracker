package com.example.personalfinancetracker.features.transaction.transactions.components

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.components.BudgetStatusBadge
import com.example.core.ui.theme.AppTheme
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.data.sync.SyncStatusEnum
import com.example.domain.model.Type
import com.example.personalfinancetracker.features.transaction.model.TransactionUi
import kotlin.math.abs

@Composable
fun TransactionCard(
    transaction: TransactionUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(AppTheme.dimensions.radiusMedium),
        border = BorderStroke(
            AppTheme.dimensions.borderThin,
            MaterialTheme.colorScheme.outline
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimensions.spacingMedium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacingMediumSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(AppTheme.dimensions.iconSizeMediumLarge)
                            .clip(CircleShape)
                            .background(transaction.currentCategory.color.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(transaction.currentCategory.icon),
                            contentDescription = null,
                            tint = transaction.currentCategory.color,
                            modifier = Modifier.size(AppTheme.dimensions.iconSizeNormal)
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(transaction.currentCategory.nameResId),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${stringResource(transaction.currentCategory.nameResId)} • ${transaction.formattedDate}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }

                    Text(
                        text = formatAmount(
                            transaction.type,
                            transaction.amount,
                            transaction.currencySymbol
                        ),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = if (transaction.type == Type.INCOME)
                            transaction.currentCategory.color
                        else
                            AppTheme.colors.expense
                    )
                }
                BudgetStatusBadge(
                    text = transaction.syncStatusEnum,
                    color = when (transaction.syncStatusEnum) {
                        SyncStatusEnum.PENDING.name ->  AppTheme.colors.expense
                        SyncStatusEnum.SYNCED.name -> AppTheme.colors.income
                        SyncStatusEnum.SYNCING.name -> MaterialTheme.colorScheme.primary
                        else -> {
                            MaterialTheme.colorScheme.outline
                        }
                    },
                    modifier = Modifier.align(Alignment.End),
                    icon = null
                )
            }
        }
    }
}


@SuppressLint("DefaultLocale")
private fun formatAmount(type: Type, amount: Double, currencySymbol: String): String {

    return if (type == Type.EXPENSE) "-${currencySymbol} ${String.format("%.2f", amount)}"
    else "+${currencySymbol} ${String.format("%.2f", abs(amount))}"
}

@Preview
@Composable
private fun CardPreview() {
    PersonalFinanceTrackerTheme {
        TransactionCard(
            transaction = TransactionUi(
                id = "1",
                userId = "1",
                amount = 100.0,
                currency = "USD",
                categoryId = "1",
                date = System.currentTimeMillis(),
                notes = "Test",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                type = Type.EXPENSE,
                syncStatusEnum = SyncStatusEnum.PENDING.name
            ),
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}
