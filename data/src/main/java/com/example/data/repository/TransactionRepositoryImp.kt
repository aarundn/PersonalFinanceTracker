package com.example.data.repository

import com.example.data.local.TrackerDatabase
import com.example.data.mapping.*
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
        transactionDB.transactionDao().getAllTransactions().map { it.toDomain() }

    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDB.transactionDao().updateTransaction(transaction.toEntity())
        syncManager.triggerImmediateSync()
    }

    override suspend fun deleteTransactionById(id: String) {
        transactionDB.transactionDao().deleteTransactionById(id)
        syncManager.triggerImmediateSync()
    }

    override suspend fun getTransactionById(id: String): Transaction? {
        return transactionDB.transactionDao().getTransactionById(id)?.toDomain()
    }

    override fun getTransactionsByBudgetId(budgetId: String): Flow<List<Transaction>> =
        transactionDB.transactionDao().getTransactionsByBudgetId(budgetId).map { it.toDomain() }


    override suspend fun syncWithRemote(): Result<Unit> = runCatching {
        pullTransactions()
        pushTransactions()
    }


    private suspend fun pullTransactions() {
        val remoteTransactions = remoteRepo.getAllTransactions().first()
        remoteTransactions.forEach { remoteDto ->
            val localEntity = transactionDB.transactionDao().getTransactionById(remoteDto.id)
            if (localEntity == null || remoteDto.updatedAt > localEntity.updatedAt) {
                transactionDB.transactionDao().insertTransaction(remoteDto.toEntity())
            }
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

