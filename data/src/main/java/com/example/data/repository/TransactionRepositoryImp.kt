package com.example.data.repository

import com.example.data.local.TransactionDao
import com.example.data.mapping.toDomain
import com.example.data.mapping.toDto
import com.example.data.mapping.toEntity
import com.example.data.remote.transaction.RemoteTransactionRepo
import com.example.data.sync.DataSyncManager
import com.example.data.sync.SyncStatusEnum
import com.example.data.util.resolveConflicts
import com.example.domain.model.Transaction
import com.example.domain.repo.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TransactionRepositoryImp(
    private val transactionDao: TransactionDao,
    private val remoteRepo: RemoteTransactionRepo,
    private val syncManager: DataSyncManager
) : TransactionRepository {


    override suspend fun addTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction.toEntity())
        syncManager.triggerImmediateSync()
    }

    override fun getAllTransactions(): Flow<List<Transaction>> =
        transactionDao.getAllTransactions()
            .map { it.toDomain() }

    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction.toEntity())
        transactionDao.updateSyncStatus(transaction.id, SyncStatusEnum.PENDING.name)
        syncManager.triggerImmediateSync()
    }

    override suspend fun deleteTransactionById(id: String) {
        transactionDao.updateSyncStatus(id, SyncStatusEnum.DELETED.name)
        syncManager.triggerImmediateSync()
    }

    override suspend fun getTransactionById(id: String): Transaction? {
        return transactionDao.getTransactionById(id)?.toDomain()
    }

    override fun getTransactionsByBudgetId(budgetId: String): Flow<List<Transaction>> =
        transactionDao.getTransactionsByBudgetId(budgetId).map { it.toDomain() }


    override suspend fun syncWithRemote(): Result<Unit> = runCatching {
        syncDeleteTransactions()
        pushTransactions()

    }

    override suspend fun resolveTransactionsConflict(): Result<Unit> = runCatching {

        val localTransactions = transactionDao.getAllTransactions().first()
        val remoteTransactions = remoteRepo.getAllTransactions()

        val resolvedTransactions = resolveConflicts(
            localData = localTransactions,
            remoteData = remoteTransactions,
            remoteMapper = { it.toEntity() },
            idMapper = { it.id },
            idMapperRemote = { it.id },
            timeMapper = { it.updatedAt },
            timeMapperRemote = { it.updatedAt }
        )

        updateTransactionsWithResolvedData(resolvedTransactions.toDomain())
    }

    private suspend fun updateTransactionsWithResolvedData(resolvedTransactions: List<Transaction>) {
        try {

            transactionDao.deleteAllTransactions()
            transactionDao.insertTransactions(resolvedTransactions.toEntity())
            println("Repository: Successfully updated Transactions with resolved data")
        } catch (e: Exception) {
            println("Repository: Failed to update messages with resolved data: ${e.message}")
        }
    }

    private suspend fun syncDeleteTransactions() {
        val localDeletedTransactions =
            transactionDao.getDeletedTransactions(SyncStatusEnum.DELETED.name)
        try {
            localDeletedTransactions.forEach { localDeleted ->
                remoteRepo.deleteTransactionById(localDeleted.id)
                transactionDao.deleteTransactionById(localDeleted.id)
            }
        } catch (e: Exception) {
            throw e
        }
    }


    private suspend fun pushTransactions() {
        val unsynced = transactionDao.getUnsyncedTransactions()
        unsynced.forEach { local ->
            transactionDao.updateSyncStatus(local.id, SyncStatusEnum.SYNCING.name)
            try {
                remoteRepo.addTransaction(local.toDto())
                transactionDao
                    .updateSyncStatus(local.id, SyncStatusEnum.SYNCED.name)
            } catch (e: Exception) {
                transactionDao
                    .updateSyncStatus(local.id, SyncStatusEnum.PENDING.name)
                throw e
            }
        }
    }
}

