package com.example.personalfinancetracker.fakes

import com.example.conversion_rate.domain.repository.ExchangeRateRepository
import com.example.conversion_rate.sync.SyncStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.math.BigDecimal

class FakeExchangeRateRepository : ExchangeRateRepository {

    // Simulates static exchange rates. E.g. USD -> EUR is 0.9
    // By default we can return a 1:1 ratio if no explicit rate is given, or map dummy rates
    private val fakeRates = mutableMapOf<Pair<String, String>, BigDecimal>()
    
    private val syncStatus = MutableStateFlow<SyncStatus>(SyncStatus.Idle)

    fun setFakeRate(fromCurrency: String, toCurrency: String, rate: BigDecimal) {
        fakeRates[Pair(fromCurrency, toCurrency)] = rate
    }

    override suspend fun convert(
        providerId: String,
        fromCurrencyCode: String,
        toCurrencyCode: String,
        amount: BigDecimal
    ): Result<BigDecimal> {
        if (fromCurrencyCode == toCurrencyCode) return Result.success(amount)
        
        val rate = fakeRates[Pair(fromCurrencyCode, toCurrencyCode)] ?: BigDecimal("1.0") // default 1:1
        return Result.success(amount * rate)
    }

    override suspend fun getProviders(): Result<List<Pair<String, String>>> {
        return Result.success(listOf(Pair("provider1", "Fake Provider")))
    }

    override suspend fun syncRate(
        providerId: String,
        fromCurrencyCode: String,
        toCurrencyCode: String
    ): Result<Unit> {
        syncStatus.value = SyncStatus.Success(System.currentTimeMillis())
        return Result.success(Unit)
    }

    override fun observeSyncStatus(): Flow<SyncStatus> {
        return syncStatus
    }
}
