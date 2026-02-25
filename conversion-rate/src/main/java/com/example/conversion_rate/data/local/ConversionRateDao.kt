package com.example.conversion_rate.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ConversionRateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRate(rate: ConversionRateEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: List<ConversionRateEntity>)

    @Query(
        """
        SELECT * FROM conversion_rates
        WHERE providerId = :providerId
          AND fromCurrency = :from
          AND toCurrency = :to
        LIMIT 1
        """
    )
    suspend fun getRate(providerId: String, from: String, to: String): ConversionRateEntity?

    @Query("DELETE FROM conversion_rates WHERE providerId = :providerId")
    suspend fun deleteByProvider(providerId: String)

    @Query("DELETE FROM conversion_rates")
    suspend fun deleteAll()
}
