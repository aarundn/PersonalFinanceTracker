package com.example.data.repository

import com.example.data.local.FakeTransactionDao
import com.example.data.local.TransactionDao
import com.example.data.local.model.TransactionEntity
import com.example.data.remote.FakeRemoteTransaction
import com.example.data.remote.model.TransactionsDto
import com.example.data.remote.transaction.RemoteTransactionRepo
import com.example.data.sync.DataSyncManager
import com.example.data.sync.FakeDataSyncManager
import com.example.domain.model.Type
import com.example.domain.repo.TransactionRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TransactionRepositoryTest {

    private lateinit var transactionRepository: TransactionRepository
    private lateinit var fakeRemoteRepo: RemoteTransactionRepo
    private lateinit var fakeTransactionDao: TransactionDao
    private lateinit var fakeSyncManager: DataSyncManager

    @Before
    fun setup() {
        fakeRemoteRepo = FakeRemoteTransaction()
        fakeTransactionDao = FakeTransactionDao()
        fakeSyncManager = FakeDataSyncManager()

        transactionRepository = TransactionRepositoryImp(
            transactionDao = fakeTransactionDao,
            remoteRepo = fakeRemoteRepo,
            syncManager = fakeSyncManager
        )
    }

    @Test
    fun `resolveTransactionsConflict - given newer remote transaction, overrides local transaction`() = runTest {

        fakeTransactionDao.insertTransaction(
            TransactionEntity(
                id = "1",
                userId = "",
                amount = 500.0,
                category = "Food",
                updatedAt = 1000L,
                syncStatus = "PENDING",
                currency = "USD",
                createdAt = 100L,
                notes = "",
                budgetId = "",
                date = 0L,
                type = Type.INCOME,
            )
        )
        fakeRemoteRepo.addTransaction(
            TransactionsDto(id = "1", amount = 500.0, updatedAt = 5000L,type = Type.INCOME.name)
        )
        assertEquals(fakeTransactionDao.getAllTransactionsOnce().size, 1)
        assertEquals(fakeRemoteRepo.getAllTransactions().size, 1)

        transactionRepository.resolveTransactionsConflict()

        assertEquals(fakeTransactionDao.getAllTransactionsOnce().size, 1)
        assertEquals(fakeTransactionDao.getAllTransactionsOnce()[0].updatedAt, 5000L)
    }

    @Test
    fun `resolveTransactionsConflict - given null remote transaction, return local transaction`() = runTest {
        fakeTransactionDao.insertTransaction(
            TransactionEntity(
                id = "1",
                userId = "",
                amount = 500.0,
                category = "Food",
                updatedAt = 1000L,
                syncStatus = "PENDING",
                currency = "USD",
                createdAt = 100L,
                notes = "",
                budgetId = "",
                date = 0L,
                type = Type.INCOME,
            )
        )
        assertEquals(fakeTransactionDao.getAllTransactionsOnce().size, 1)
        assertEquals(fakeRemoteRepo.getAllTransactions().size, 0)

        transactionRepository.resolveTransactionsConflict()

        assertEquals(fakeTransactionDao.getAllTransactionsOnce().size, 1)
    }

    @Test
    fun `resolveTransactionsConflict - given null local transaction, return remote transaction`() = runTest {
        fakeRemoteRepo.addTransaction(
            TransactionsDto(
                id = "1",
                userId = "",
                amount = 500.0,
                category = "Food",
                updatedAt = 1000L,
                currency = "USD",
                createdAt = 100L,
                type = Type.INCOME.name,
            )
        )
        assertEquals(fakeTransactionDao.getAllTransactionsOnce().size, 0)
        assertEquals(fakeRemoteRepo.getAllTransactions().size, 1)


        transactionRepository.resolveTransactionsConflict()

        assertEquals(fakeTransactionDao.getAllTransactionsOnce().size, 1)
    }
}