package com.example.data.repository

import android.util.Log
import com.example.data.local.TrackerDatabase
import com.example.data.mapping.*
import com.example.data.remote.budget.RemoteBudgetRepo
import com.example.data.sync.DataSyncManager
import com.example.data.sync.SyncStatusEnum
import com.example.domain.model.Budget
import com.example.domain.repo.BudgetRepository
import kotlinx.coroutines.flow.Flow
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
        trackerDB.budgetDao().updateSyncStatus(budget.id, SyncStatusEnum.PENDING.name)
        syncManager.triggerImmediateSync()
    }

    override suspend fun deleteBudgetById(id: String) {
        trackerDB.budgetDao().updateSyncStatus(id, SyncStatusEnum.DELETED.name)
        syncManager.triggerImmediateSync()
    }

    override suspend fun getBudgetById(id: String): Budget? {
        return trackerDB.budgetDao().getBudgetById(id)?.toDomain()
    }

    override fun getBudgetsByCategory(category: String): Flow<List<Budget>> =
        trackerDB.budgetDao().getBudgetsByCategory(category).map { it.toDomain() }

    override suspend fun syncWithRemote(): Result<Unit> = runCatching {
        syncDeleteBudgets()
        pushBudgets()
    }

    override suspend fun resolveBudgetsConflict(): Result<Unit> = runCatching {
        val resolvedBudgets = mutableListOf<Budget>()
        val localBudgets = trackerDB.budgetDao().getAllBudgetsOnce()
        val remoteBudgets = remoteRepo.getAllBudgetsOnce()

        // Create maps for efficient lookup
        val localMap = localBudgets.associateBy { it.id }
        val remoteMap = remoteBudgets.associateBy { it.id }

        // Process all unique Budget IDs
        val allIds = (localBudgets.map { it.id } + remoteBudgets.map { it.id }).distinct()

        for (id in allIds) {
            val localBudget = localMap[id]
            val remoteBudget = remoteMap[id]

            when {
                localBudget == null && remoteBudget != null -> {
                    resolvedBudgets.add(remoteBudget.toEntity().toDomain())
                }
                localBudget != null && remoteBudget == null -> {
                    resolvedBudgets.add(localBudget.toDomain())
                }
                localBudget != null && remoteBudget != null -> {
                    if (localBudget.updatedAt > remoteBudget.updatedAt) {
                        resolvedBudgets.add(localBudget.toDomain())
                    } else {
                        resolvedBudgets.add(remoteBudget.toEntity().toDomain())
                    }
                }
            }
        }
        updateBudgetsWithResolvedData(resolvedBudgets)
    }

    private suspend fun syncDeleteBudgets() {
        val localDeletedBudgets = trackerDB.budgetDao().getDeletedBudgets(SyncStatusEnum.DELETED.name)
        localDeletedBudgets.forEach { localDeleted ->
            try {
                remoteRepo.deleteBudgetById(localDeleted.id)
                trackerDB.budgetDao().deleteBudgetById(localDeleted.id)
            } catch (e: Exception) {
                Log.e("BudgetRepository", "Failed to delete budget: ${e.message}")
            }
        }
    }

    private suspend fun pushBudgets() {
        val unsyncedList = trackerDB.budgetDao().getUnsyncedBudgets()
        unsyncedList.forEach { local ->
            trackerDB.budgetDao().updateSyncStatus(local.id, SyncStatusEnum.SYNCING.name)
            try {
                remoteRepo.addBudget(local.toDto())
                trackerDB.budgetDao().updateSyncStatus(local.id, SyncStatusEnum.SYNCED.name)
            } catch (e: Exception) {
                trackerDB.budgetDao().updateSyncStatus(local.id, SyncStatusEnum.PENDING.name)
                Log.e("BudgetRepository", "Failed to push budget: ${e.message}")
            }
        }
    }

    private suspend fun updateBudgetsWithResolvedData(resolvedBudgets: List<Budget>) {
        try {
            trackerDB.budgetDao().deleteAllBudgets()
            resolvedBudgets.forEach { budget ->
                trackerDB.budgetDao().insertBudget(budget.toEntity())
            }
            Log.d("BudgetRepository", "Successfully updated budgets with resolved data")
        } catch (e: Exception) {
            Log.e("BudgetRepository", "Failed to update budgets with resolved data: ${e.message}")
        }
    }
}
