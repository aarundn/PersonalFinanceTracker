package com.example.data.repository

import com.example.data.local.TrackerDatabase
import com.example.data.mapping.*
import com.example.data.remote.budget.RemoteBudgetRepo
import com.example.data.sync.DataSyncManager
import com.example.data.sync.SyncStatusEnum
import com.example.domain.model.Budget
import com.example.domain.repo.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class BudgetRepositoryImp(
    private val trackerDB: TrackerDatabase,
    private val remoteRepo: RemoteBudgetRepo,
    private val syncManager: DataSyncManager
) : BudgetRepository {

    override suspend fun addBudget(budget: Budget) {
        trackerDB.budgetDao().insertBudget(budget.toEntity())
        syncManager.triggerImmediateSync()
    }

    override fun getAllBudgets(): Flow<List<Budget>> =
        trackerDB.budgetDao().getAllBudgets().map { it.toDomain() }

    override suspend fun updateBudget(budget: Budget) {
        trackerDB.budgetDao().updateBudget(budget.toEntity())
        syncManager.triggerImmediateSync()
    }

    override suspend fun deleteBudgetById(id: String) {
        trackerDB.budgetDao().deleteBudgetById(id)
        syncManager.triggerImmediateSync()
    }

    override suspend fun getBudgetById(id: String): Budget? {
        return trackerDB.budgetDao().getBudgetById(id)?.toDomain()
    }

    override fun getBudgetsByCategory(category: String): Flow<List<Budget>> =
        trackerDB.budgetDao().getBudgetsByCategory(category).map { it.toDomain() }

    override suspend fun syncWithRemote(): Result<Unit> = runCatching {
        pullTransactions()
        pushTransactions()
    }

    private suspend fun pushTransactions() {

        val unsyncedList = trackerDB.budgetDao().getUnsyncedBudgets()
        unsyncedList.forEach { local ->

            trackerDB.budgetDao().updateSyncStatus(local.id, SyncStatusEnum.SYNCING.name)
            try {
                remoteRepo.addBudget(local.toDto())
                trackerDB.budgetDao().updateSyncStatus(local.id, SyncStatusEnum.SYNCED.name)
            } catch (e: Exception) {

                trackerDB.budgetDao().updateSyncStatus(local.id, SyncStatusEnum.PENDING.name)
                throw e
            }
        }
    }

    private suspend fun pullTransactions() {
        val remoteBudgets = remoteRepo.getAllBudgets().first()
        remoteBudgets.forEach { remoteDto ->
            val localEntity = trackerDB.budgetDao().getBudgetById(remoteDto.id)
            if (localEntity == null || remoteDto.updatedAt > localEntity.updatedAt) {

                trackerDB.budgetDao().insertBudget(remoteDto.toEntity())
            }
        }
    }
}
