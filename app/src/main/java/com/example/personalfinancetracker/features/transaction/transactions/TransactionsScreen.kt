package com.example.personalfinancetracker.features.transaction.transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.core.components.LoadingIndicator
import com.example.personalfinancetracker.features.transaction.transactions.components.EmptyState
import com.example.personalfinancetracker.features.transaction.transactions.components.HeaderSection
import com.example.personalfinancetracker.features.transaction.transactions.components.TransactionCard

@Composable
fun TransactionsScreen(
    transactionsUiState: TransactionsUiState,
    onEvent: (TransactionsEvent) -> Unit,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            val state = transactionsUiState as? TransactionsUiState.Success
            if (state?.transactions?.isEmpty() == false)
                HeaderSection(
                    title = "Recent Transactions",
                    onAddClick = { onEvent(TransactionsEvent.OnAddTransactionClick) }
                )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (transactionsUiState) {
                is TransactionsUiState.Loading -> LoadingIndicator()

                is TransactionsUiState.Error -> {
                    Text(
                        text = transactionsUiState.message,
                        color = MaterialTheme.colorScheme.error,
                    )
                }

                is TransactionsUiState.Success -> {
                    if (transactionsUiState.transactions.isEmpty()) {
                        EmptyState(
                            onAddClick = { onEvent(TransactionsEvent.OnAddTransactionClick) }
                        )
                    } else {

                        Content(transactionsUiState, onEvent)

                        FloatingActionButton(
                            onClick = { onEvent(TransactionsEvent.OnAddTransactionClick) },
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
            }
        }
    }
}

@Composable
private fun Content(
    transactionsUiState: TransactionsUiState.Success,
    onEvent: (TransactionsEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            items(
                transactionsUiState.transactions,
                key = { it.id }) { transaction ->
                TransactionCard(
                    transaction = transaction,
                    onClick = { onEvent(TransactionsEvent.OnTransactionClick(transaction)) }
                )
            }
        }
    }
}