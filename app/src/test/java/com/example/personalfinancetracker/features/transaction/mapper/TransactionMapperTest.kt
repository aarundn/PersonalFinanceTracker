package com.example.personalfinancetracker.features.transaction.mapper

import com.example.data.sync.SyncStatusEnum
import com.example.domain.model.Transaction
import com.example.domain.model.Type
import com.example.personalfinancetracker.features.transaction.model.TransactionUi
import org.junit.Assert.assertEquals
import org.junit.Test

class TransactionMapperTest {

    private val domainModel = Transaction(
        id = "tx123",
        userId = "user1",
        amount = 50.0,
        currency = "USD",
        category = "Food",
        date = 1672531200000L,
        notes = "Lunch at cafe",
        createdAt = 1672531200000L,
        updatedAt = 1672531200000L,
        type = Type.EXPENSE,
        budgetId = "budget1",
        syncStatus = SyncStatusEnum.PENDING.name
    )

    private val uiModel = TransactionUi(
        id = "tx123",
        userId = "user1",
        amount = 50.0,
        currency = "USD",
        categoryId = "Food",
        date = 1672531200000L,
        notes = "Lunch at cafe",
        createdAt = 1672531200000L,
        updatedAt = 1672531200000L,
        type = Type.EXPENSE,
        budgetId = "budget1",
        syncStatusEnum = SyncStatusEnum.PENDING.name
    )

    @Test
    fun `given Transaction, when toTransactionUi is called, then return TransactionUi correctly`() {
        val result = domainModel.toTransactionUi()

        assertEquals(domainModel.id, result.id)
        assertEquals(domainModel.userId, result.userId)
        assertEquals(domainModel.amount, result.amount, 0.0)
        assertEquals(domainModel.currency, result.currency)
        assertEquals(domainModel.category, result.categoryId)
        assertEquals(domainModel.date, result.date)
        assertEquals(domainModel.notes, result.notes)
        assertEquals(domainModel.createdAt, result.createdAt)
        assertEquals(domainModel.updatedAt, result.updatedAt)
        assertEquals(domainModel.type, result.type)
        assertEquals(domainModel.budgetId, result.budgetId)
        assertEquals(domainModel.syncStatus, result.syncStatusEnum)
    }

    @Test
    fun `given TransactionUi, when toTransaction is called, then return Transaction correctly`() {
        val result = uiModel.toTransaction()

        assertEquals(uiModel.id, result.id)
        assertEquals(uiModel.userId, result.userId)
        assertEquals(uiModel.amount, result.amount, 0.0)
        assertEquals(uiModel.currency, result.currency)
        assertEquals(uiModel.categoryId, result.category)
        assertEquals(uiModel.date, result.date)
        assertEquals(uiModel.notes, result.notes)
        assertEquals(uiModel.createdAt, result.createdAt)
        assertEquals(uiModel.updatedAt, result.updatedAt)
        assertEquals(uiModel.type, result.type)
        assertEquals(uiModel.budgetId, result.budgetId)
        assertEquals(uiModel.syncStatusEnum, result.syncStatus)
    }

    @Test
    fun `given List of Transaction, when toTransactionUi is called on list, then return List of TransactionUi correctly`() {
        val domainList = listOf(domainModel)
        
        val resultList = domainList.toTransactionUi()

        assertEquals(1, resultList.size)
        assertEquals(domainModel.id, resultList[0].id)
    }
}
