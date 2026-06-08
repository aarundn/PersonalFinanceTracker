package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.core.ui.theme.AppColorTokens
import com.example.core.ui.theme.AppTheme
import com.example.personalfinancetracker.features.transaction.model.TransactionUi
import com.example.personalfinancetracker.features.transaction.transactions.components.TransactionCard

@Composable
fun TransactionsList(
    transactions: List<TransactionUi>,
    onViewAllClick: () -> Unit,
    onTransactionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "Recent Transactions",
                style = MaterialTheme.typography.titleLarge,
                color = AppColorTokens.White
            )
            Text(
                text = "View All",
                style = MaterialTheme.typography.bodyMedium,
                color = AppColorTokens.Gray400,
                modifier = Modifier.clickable(onClick = onViewAllClick)
            )
        }
        
        Spacer(modifier = Modifier.height(AppTheme.dimensions.spacingMedium))
        
        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacingSmall)
        ) {
            transactions.forEach { transaction ->
                TransactionCard(
                    transaction = transaction,
                    onClick = { onTransactionClick(transaction.id) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
