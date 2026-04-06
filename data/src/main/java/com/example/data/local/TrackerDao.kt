package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.data.local.model.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions ORDER BY date DESC ")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions ORDER BY date DESC ")
    fun getAllTransactionsOnce(): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE category = :category ORDER BY date DESC")
    fun getTransactionsByCategory(category: String): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionEntity>)


    @Upsert
    suspend fun upsertTransaction(transaction: List<TransactionEntity>)


    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: String)

    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()


    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: String): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE budgetId = :budgetId ORDER BY date DESC")
    fun getTransactionsByBudgetId(budgetId: String): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE  syncStatus NOT IN ('SYNCED', 'DELETED')")
    suspend fun getUnsyncedTransactions(): List<TransactionEntity>

    @Query("UPDATE transactions SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)

    @Query("SELECT * FROM transactions WHERE syncStatus = :status")
    suspend fun getDeletedTransactions(status: String): List<TransactionEntity>
}