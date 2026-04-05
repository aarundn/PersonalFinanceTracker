package com.example.data.mapping

import com.example.data.local.model.BudgetEntity
import com.example.data.remote.model.BudgetDto
import com.example.data.sync.SyncStatusEnum
import com.example.domain.model.Budget
import junit.framework.TestCase.assertEquals
import org.junit.Test

class BudgetMapperTest {

    val entity = BudgetEntity(
        id = "1",
        userId = "user1",
        category = "food",
        amount = 100.0,
        currency = "USD",
        period = "weekly",
        notes = "test notes",
        createdAt = 1234567890,
        updatedAt = 1234567891,
        syncStatus = "PENDING"
    )

    @Test
    fun `given BudgetEntity, when toDomain is called, then return Budget correctly`() {

        val domain = entity.toDomain()

        assertEquals(entity.id, domain.id)
        assertEquals(entity.userId, domain.userId)
        assertEquals(entity.category, domain.category)
        assertEquals(entity.amount, domain.amount, 0.0)
        assertEquals(entity.currency, domain.currency)
        assertEquals(entity.period, domain.period)
        assertEquals(entity.notes, domain.notes)
        assertEquals(entity.createdAt, domain.createdAt)
        assertEquals(entity.updatedAt, domain.updatedAt)
        assertEquals(entity.syncStatus, domain.syncStatus)
    }

    @Test
    fun `given BudgetEntity, when toDto is called, then return BudgetDto correctly`() {

        val dto = entity.toDto()

        assertEquals(entity.id, dto.id)
        assertEquals(entity.userId, dto.userId)
        assertEquals(entity.category, dto.category)
        assertEquals(entity.amount, dto.amount, 0.0)
        assertEquals(entity.currency, dto.currency)
        assertEquals(entity.period, dto.period)
        assertEquals(entity.notes, dto.notes)
        assertEquals(entity.createdAt, dto.createdAt)
        assertEquals(entity.updatedAt, dto.updatedAt)
        assertEquals(entity.syncStatus, SyncStatusEnum.PENDING.name)
    }

    @Test
    fun `given BudgetDto, when toEntity is called, then return BudgetEntity correctly`() {
        val dto = BudgetDto(
            id = "1",
            userId = "user1",
            category = "food",
            amount = 100.0,
            currency = "USD",
            period = "weekly",
            notes = "test notes",
            createdAt = 1234567890,
            updatedAt = 1234567891,
        )

        val entity = dto.toEntity()

        assertEquals(dto.id, entity.id)
        assertEquals(dto.userId, entity.userId)
        assertEquals(dto.category, entity.category)
        assertEquals(dto.amount, entity.amount, 0.0)
        assertEquals(dto.currency, entity.currency)
        assertEquals(dto.period, entity.period)
        assertEquals(dto.notes, entity.notes)
        assertEquals(dto.createdAt, entity.createdAt)
        assertEquals(dto.updatedAt, entity.updatedAt) }
    @Test
    fun `given Budget, when toEntity is called, then return BudgetEntity correctly`() {

        val domain = Budget(
            id = "1",
            userId = "user1",
            category = "food",
            amount = 100.0,
            currency = "USD",
            period = "weekly",
            notes = "test notes",
            createdAt = 1234567890,
            updatedAt = 1234567891,
            syncStatus = "PENDING"
        )

        val entity = domain.toEntity()

        assertEquals(domain.id, entity.id)
        assertEquals(domain.userId, entity.userId)
        assertEquals(domain.category, entity.category)
        assertEquals(domain.amount, entity.amount, 0.0)
        assertEquals(domain.currency, entity.currency)
        assertEquals(domain.period, entity.period)
        assertEquals(domain.notes, entity.notes)
        assertEquals(domain.createdAt, entity.createdAt)
        assertEquals(domain.updatedAt, entity.updatedAt)
        assertEquals(domain.syncStatus, entity.syncStatus)
    }
}








































