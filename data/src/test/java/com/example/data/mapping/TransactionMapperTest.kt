package com.example.data.mapping

import com.example.data.local.model.TransactionEntity
import com.example.data.remote.model.TransactionsDto
import com.example.data.sync.SyncStatusEnum
import com.example.domain.model.Transaction
import com.example.domain.model.Type
import org.junit.Assert.assertEquals
import org.junit.Test

class TransactionMapperTest {

    private val entity = TransactionEntity(
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

    @Test
    fun `given TransactionEntity, when toDomain is called, then return Transaction correctly`() {
        // WHEN
        val domain = entity.toDomain()

        // THEN
        assertEquals(entity.id, domain.id)
        assertEquals(entity.userId, domain.userId)
        assertEquals(entity.amount, domain.amount, 0.0)
        assertEquals(entity.currency, domain.currency)
        assertEquals(entity.category, domain.category)
        assertEquals(entity.date, domain.date)
        assertEquals(entity.notes, domain.notes)
        assertEquals(entity.createdAt, domain.createdAt)
        assertEquals(entity.updatedAt, domain.updatedAt)
        assertEquals(entity.type, domain.type)
        assertEquals(entity.budgetId, domain.budgetId)
        assertEquals(entity.syncStatus, domain.syncStatus)
    }

    @Test
    fun `given TransactionEntity, when toDto is called, then return TransactionsDto correctly`() {
        // WHEN
        val dto = entity.toDto()

        // THEN
        assertEquals(entity.id, dto.id)
        assertEquals(entity.userId, dto.userId)
        assertEquals(entity.amount, dto.amount, 0.0)
        assertEquals(entity.currency, dto.currency)
        assertEquals(entity.category, dto.category)
        assertEquals(entity.date, dto.date)
        assertEquals(entity.notes, dto.description) // Mapping from notes to description
        assertEquals(entity.createdAt, dto.createdAt)
        assertEquals(entity.updatedAt, dto.updatedAt)
        assertEquals(entity.type.name, dto.type)
        assertEquals(entity.budgetId, dto.budgetId)
    }

    @Test
    fun `given TransactionsDto, when toEntity is called, then return TransactionEntity with SYNCED status`() {
        // GIVEN
        val dto = TransactionsDto(
            id = "tx456",
            userId = "user1",
            amount = 75.0,
            currency = "EUR",
            description = "Dinner with friends",
            date = 1672617600000L,
            category = "Leisure",
            type = "EXPENSE",
            createdAt = 1672617600000L,
            updatedAt = 1672617600000L,
            budgetId = "budget2"
        )

        // WHEN
        val entity = dto.toEntity()

        // THEN
        assertEquals(dto.id, entity.id)
        assertEquals(dto.userId, entity.userId)
        assertEquals(dto.amount, entity.amount, 0.0)
        assertEquals(dto.currency, entity.currency)
        assertEquals(dto.category, entity.category)
        assertEquals(dto.date, entity.date)
        assertEquals(dto.description, entity.notes)
        assertEquals(dto.createdAt, entity.createdAt)
        assertEquals(dto.updatedAt, entity.updatedAt)
        assertEquals(Type.EXPENSE, entity.type)
        assertEquals(dto.budgetId, entity.budgetId)
        assertEquals(SyncStatusEnum.SYNCED.name, entity.syncStatus)
    }

    @Test
    fun `given Transaction, when toEntity is called, then return TransactionEntity correctly`() {
        // GIVEN
        val domain = Transaction(
            id = "tx789",
            userId = "user1",
            amount = 100.0,
            currency = "USD",
            category = "Salary",
            date = 1672704000000L,
            notes = "Monthly salary",
            createdAt = 1672704000000L,
            updatedAt = 1672704000000L,
            type = Type.INCOME,
            budgetId = null,
            syncStatus = SyncStatusEnum.SYNCED.name
        )

        // WHEN
        val entity = domain.toEntity()

        // THEN
        assertEquals(domain.id, entity.id)
        assertEquals(domain.userId, entity.userId)
        assertEquals(domain.amount, entity.amount, 0.0)
        assertEquals(domain.currency, entity.currency)
        assertEquals(domain.category, entity.category)
        assertEquals(domain.date, entity.date)
        assertEquals(domain.notes, entity.notes)
        assertEquals(domain.createdAt, entity.createdAt)
        assertEquals(domain.updatedAt, entity.updatedAt)
        assertEquals(domain.type, entity.type)
        assertEquals(domain.budgetId, entity.budgetId)
        assertEquals(domain.syncStatus, entity.syncStatus)
    }

    @Test
    fun `given List of TransactionEntity, when toDomain is called, then return List of Transaction correctly`() {
        // GIVEN
        val entityList = listOf(entity)

        // WHEN
        val domainList = entityList.toDomain()

        // THEN
        assertEquals(1, domainList.size)
        assertEquals(entity.id, domainList[0].id)
    }
}
