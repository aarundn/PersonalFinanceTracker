package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.model.CategoryEntity
import com.example.data.local.model.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        CategoryEntity::class
    ],
    version = 4,
    exportSchema = true
)

abstract class TrackerDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    companion object {
        const val DATABASE_NAME = "tracker.db"
    }
}