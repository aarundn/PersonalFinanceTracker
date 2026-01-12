package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.model.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
    ],
    version = 1,
    exportSchema = true
)

abstract class TrackerDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao

    companion object {
        const val DATABASE_NAME = "tracker.db"
    }
}