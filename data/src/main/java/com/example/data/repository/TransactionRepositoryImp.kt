package com.example.data.repository

import android.util.Log
import com.example.data.local.TrackerDatabase
import com.example.data.mapping.toDomain
import com.example.data.mapping.toDto
import com.example.data.mapping.toEntity
import com.example.data.remote.transaction.RemoteTransactionRepo
import com.example.data.sync.DataSyncManager
import com.example.data.sync.SyncStatusEnum
import com.example.domain.model.Transaction
import com.example.domain.repo.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TransactionRepositoryImp(
    private val transactionDB: TrackerDatabase,
    private val remoteRepo: RemoteTransactionRepo,
    private val syncManager: DataSyncManager
) : TransactionRepository {


    override suspend fun addTransaction(transaction: Transaction) {
        transactionDB.transactionDao().insertTransaction(transaction.toEntity())
        syncManager.triggerImmediateSync()
    }

    override fun getAllTransactions(): Flow<List<Transaction>> =
        transactionDB.transactionDao().getAllTransactions()
            .map { it.toDomain() }

    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDB.transactionDao().updateTransaction(transaction.toEntity())
        transactionDB.transactionDao().updateSyncStatus(transaction.id, SyncStatusEnum.PENDING.name)
        syncManager.triggerImmediateSync()
    }

    override suspend fun deleteTransactionById(id: String) {
        transactionDB.transactionDao().updateSyncStatus(id, SyncStatusEnum.DELETED.name)
        syncManager.triggerImmediateSync()
    }

    override suspend fun getTransactionById(id: String): Transaction? {
        return transactionDB.transactionDao().getTransactionById(id)?.toDomain()
    }

    override fun getTransactionsByBudgetId(budgetId: String): Flow<List<Transaction>> =
        transactionDB.transactionDao().getTransactionsByBudgetId(budgetId).map { it.toDomain() }


    override suspend fun syncWithRemote(): Result<Unit> = runCatching {
        syncDeleteTransactions()
        pushTransactions()

    }

    override suspend fun resolveTransactionsConflict(): Result<Unit> = runCatching {

        val resolvedTransactions = mutableListOf<Transaction>()
        val localTransactions = transactionDB.transactionDao().getAllTransactions().first()
        val remoteTransactions = remoteRepo.getAllTransactions()


        // Create maps for efficient lookup
        val localMessageMap = localTransactions.associateBy { it.id }
        val remoteMessageMap = remoteTransactions.associateBy { it.id }

        // Process all unique Transactions IDs
        val allMessageIds =
            (localTransactions.map { it.id } + remoteTransactions.map { it.id }).distinct()

        for (messageId in allMessageIds) {
            val localTransaction = localMessageMap[messageId]
            val remoteTransaction = remoteMessageMap[messageId]
            when {
                localTransaction == null && remoteTransaction != null -> {
                    resolvedTransactions.add(remoteTransaction.toEntity().toDomain())
                }

                localTransaction != null && remoteTransaction == null -> {
                    resolvedTransactions.add(localTransaction.toDomain())
                }

                localTransaction != null && remoteTransaction != null -> {
                    if (localTransaction.updatedAt > remoteTransaction.updatedAt) {
                        resolvedTransactions.add(localTransaction.toDomain())
                    } else {
                        resolvedTransactions.add(remoteTransaction.toEntity().toDomain())
                    }
                }
            }
            Log.d("TransactionRepository", "Resolved Transactions: ${resolvedTransactions.size}")
        }
        updateTransactionsWithResolvedData(resolvedTransactions)
    }

    private suspend fun updateTransactionsWithResolvedData(resolvedTransactions: List<Transaction>) {
        try {

            transactionDB.transactionDao().deleteAllTransactions()
            resolvedTransactions.forEach { transaction ->
                transactionDB.transactionDao().insertTransaction(transaction.toEntity())
            }
            println("Repository: Successfully updated Transactions with resolved data")
        } catch (e: Exception) {
            println("Repository: Failed to update messages with resolved data: ${e.message}")
        }
    }

    private suspend fun syncDeleteTransactions() {
        val localDeletedTransactions =
            transactionDB.transactionDao().getDeletedTransactions(SyncStatusEnum.DELETED.name)
        try {
            localDeletedTransactions.forEach { localDeleted ->
                remoteRepo.deleteTransactionById(localDeleted.id)
                transactionDB.transactionDao().deleteTransactionById(localDeleted.id)
            }
        } catch (e: Exception) {
            throw e
        }
    }


    private suspend fun pushTransactions() {
        val unsynced = transactionDB.transactionDao().getUnsyncedTransactions()
        unsynced.forEach { local ->
            transactionDB.transactionDao().updateSyncStatus(local.id, SyncStatusEnum.SYNCING.name)
            try {
                remoteRepo.addTransaction(local.toDto())
                transactionDB.transactionDao()
                    .updateSyncStatus(local.id, SyncStatusEnum.SYNCED.name)
            } catch (e: Exception) {
                transactionDB.transactionDao()
                    .updateSyncStatus(local.id, SyncStatusEnum.PENDING.name)
                throw e
            }
        }
    }
}

