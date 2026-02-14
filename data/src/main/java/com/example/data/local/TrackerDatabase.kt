package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.model.BudgetEntity
import com.example.data.local.model.CategoryEntity
import com.example.data.local.model.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        CategoryEntity::class,
        BudgetEntity::class
    ],
    version = 9,
    exportSchema = true
)

abstract class TrackerDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetDao(): BudgetDao
    companion object {
        const val DATABASE_NAME = "tracker.db"
    }
}