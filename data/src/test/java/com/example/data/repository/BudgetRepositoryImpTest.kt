package com.example.data.repository

import com.example.data.local.BudgetDao
import com.example.data.local.FakeBudgetDao
import com.example.data.local.model.BudgetEntity
import com.example.data.remote.FakeRemoteBudget
import com.example.data.remote.budget.RemoteBudgetRepo
import com.example.data.remote.model.BudgetDto
import com.example.data.sync.DataSyncManager
import com.example.data.sync.FakeDataSyncManager
import com.example.domain.repo.BudgetRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class BudgetRepositoryImpTest {

    private lateinit var budgetRepository: BudgetRepository
    private lateinit var budgetDao: BudgetDao
    private lateinit var remoteRepo: RemoteBudgetRepo
    private lateinit var syncManager: DataSyncManager


    @Before
    fun setup() {
        budgetDao = FakeBudgetDao()
        remoteRepo = FakeRemoteBudget()
        syncManager = FakeDataSyncManager()
        budgetRepository = BudgetRepositoryImp(budgetDao, remoteRepo, syncManager)


    }

    @Test
    fun `resolveBudgetsConflict - given newer remote budget, overrides local budget`() = runTest {

        budgetDao.insertBudget(
            BudgetEntity(
                id = "1",
                userId = "",
                amount = 500.0,
                category = "Food",
                updatedAt = 1000L,
                syncStatus = "PENDING",
                currency = "USD",
                createdAt = 100L,
                period = "",
                notes = ""
            )
        )
        remoteRepo.addBudget(
            BudgetDto(id = "1", amount = 500.0, updatedAt = 5000L)
        )
        assertEquals(budgetDao.getAllBudgetsOnce().size, 1)
        assertEquals(remoteRepo.getAllBudgetsOnce().size, 1)
        budgetRepository.resolveBudgetsConflict()
        assertEquals(budgetDao.getAllBudgetsOnce().size, 1)
        assertEquals(budgetDao.getAllBudgetsOnce()[0].updatedAt, 5000L)
    }

    @Test
    fun `resolveBudgetsConflict - given null remote budget, return local budget`() = runTest {
        budgetDao.insertBudget(
            BudgetEntity(
                id = "1",
                userId = "",
                amount = 500.0,
                category = "Food",
                updatedAt = 1000L,
                syncStatus = "PENDING",
                currency = "USD",
                createdAt = 100L,
                period = "",
                notes = ""
            )
        )
        assertEquals(budgetDao.getAllBudgetsOnce().size, 1)
        assertEquals(remoteRepo.getAllBudgetsOnce().size, 0)

        budgetRepository.resolveBudgetsConflict()

        assertEquals(budgetDao.getAllBudgetsOnce().size, 1)
    }

    @Test
    fun `resolveBudgetsConflict - given null local budget, return remote budget`() = runTest {
        remoteRepo.addBudget(
            BudgetDto(
                id = "1",
                userId = "",
                amount = 500.0,
                category = "Food",
                updatedAt = 1000L,
                currency = "USD",
                createdAt = 100L,
                period = "",
                notes = ""
            )
        )
        assertEquals(budgetDao.getAllBudgetsOnce().size, 0)
        assertEquals(remoteRepo.getAllBudgetsOnce().size, 1)

        budgetRepository.resolveBudgetsConflict()

        assertEquals(budgetDao.getAllBudgetsOnce().size, 1)
    }

}

