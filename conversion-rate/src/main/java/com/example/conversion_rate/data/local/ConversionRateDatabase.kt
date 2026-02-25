package com.example.conversion_rate.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ConversionRateEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ConversionRateDatabase : RoomDatabase() {

    abstract fun conversionRateDao(): ConversionRateDao

    companion object {
        const val DATABASE_NAME = "Conversion.db"
    }
}
