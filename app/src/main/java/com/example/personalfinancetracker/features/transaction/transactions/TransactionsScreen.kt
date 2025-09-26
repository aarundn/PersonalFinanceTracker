package com.example.personalfinancetracker.features.transaction.transactions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.personalfinancetracker.features.transaction.transactions.components.EmptyState
import com.example.personalfinancetracker.features.transaction.transactions.components.HeaderSection
import com.example.personalfinancetracker.features.transaction.transactions.components.TransactionCard

// Transaction data class is now in TransactionsContract

@Composable
fun TransactionsScreen(
    state: TransactionsContract.State,
    onEvent: (TransactionsContract.Event) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            state.transactions.isEmpty() -> {
                EmptyState(
                    onAddClick = { onEvent(TransactionsContract.Event.OnAddTransactionClick) }
                )
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    HeaderSection(
                        title = "Recent Transactions",
                        onAddClick = { onEvent(TransactionsContract.Event.OnAddTransactionClick) }
                    )

                    // Transactions List
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 88.dp) // Space for FAB
                    ) {
                        items(state.transactions) { transaction ->
                            TransactionCard(
                                transaction = transaction,
                                onClick = { onEvent(TransactionsContract.Event.OnTransactionClick(transaction)) }
                            )
                        }
                    }
                }

                // Floating Action Button
                FloatingActionButton(
                    onClick = { onEvent(TransactionsContract.Event.OnAddTransactionClick) },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(24.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Transaction"
                    )
                }
            }
        }

        // Error Handling
        state.error?.let { error ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
            ) {
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}