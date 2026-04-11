package com.example.personalfinancetracker.features.budget.mapper

import com.example.data.sync.SyncStatusEnum
import com.example.domain.model.Budget
import com.example.personalfinancetracker.features.budget.model.BudgetUi
import org.junit.Assert.assertEquals
import org.junit.Test

class BudgetMapperTest {

    private val domainModel = Budget(
        id = "1",
        userId = "user1",
        category = "food",
        amount = 100.0,
        currency = "USD",
        period = "weekly",
        notes = "test notes",
        createdAt = 1234567890L,
        updatedAt = 1234567891L,
        syncStatus = SyncStatusEnum.PENDING.name
    )

    private val uiModel = BudgetUi(
        id = "1",
        userId = "user1",
        category = "food",
        amount = 100.0,
        currency = "USD",
        period = "weekly",
        notes = "test notes",
        createdAt = 1234567890L,
        updatedAt = 1234567891L,
        syncStatusEnum = SyncStatusEnum.PENDING.name,
        spent = 40.0
    )

    @Test
    fun `given Budget, when toBudgetUi is called, then return BudgetUi correctly`() {
        val spentAmount = 40.0
        val result = domainModel.toBudgetUi(spent = spentAmount)

        assertEquals(domainModel.id, result.id)
        assertEquals(domainModel.userId, result.userId)
        assertEquals(domainModel.category, result.category)
        assertEquals(domainModel.amount, result.amount, 0.0)
        assertEquals(domainModel.currency, result.currency)
        assertEquals(domainModel.period, result.period)
        assertEquals(domainModel.notes, result.notes)
        assertEquals(domainModel.createdAt, result.createdAt)
        assertEquals(domainModel.updatedAt, result.updatedAt)
        assertEquals(domainModel.syncStatus, result.syncStatusEnum)
        assertEquals(spentAmount, result.spent, 0.0)
    }

    @Test
    fun `given BudgetUi, when toBudget is called, then return Budget correctly`() {
        val result = uiModel.toBudget()

        assertEquals(uiModel.id, result.id)
        assertEquals(uiModel.userId, result.userId)
        assertEquals(uiModel.category, result.category)
        assertEquals(uiModel.amount, result.amount, 0.0)
        assertEquals(uiModel.currency, result.currency)
        assertEquals(uiModel.period, result.period)
        assertEquals(uiModel.notes, result.notes)
        assertEquals(uiModel.createdAt, result.createdAt)
        assertEquals(uiModel.updatedAt, result.updatedAt)
        assertEquals(uiModel.syncStatusEnum, result.syncStatus)
    }

    @Test
    fun `given BudgetUi, when toDisplayData is called, then return BudgetDisplayData correctly`() {
        val result = uiModel.toDisplayData()

        assertEquals(uiModel.spent, result.spent, 0.0)
        assertEquals(uiModel.amount, result.amount, 0.0)
        assertEquals(uiModel.isOverBudget, result.isOverBudget)
        assertEquals(uiModel.isWarning, result.isWarning)
        assertEquals(uiModel.overBudget, result.overBudget, 0.0)
        assertEquals(uiModel.remaining, result.remaining, 0.0)
        assertEquals(uiModel.currencySymbol, result.currencySymbol)
    }
}
