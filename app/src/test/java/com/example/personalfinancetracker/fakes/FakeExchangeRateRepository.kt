package com.example.personalfinancetracker.fakes

import com.example.conversion_rate.domain.repository.ExchangeRateRepository
import com.example.conversion_rate.sync.SyncStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.math.BigDecimal

class FakeExchangeRateRepository : ExchangeRateRepository {


    private val fakeRates = mutableMapOf<Pair<String, String>, BigDecimal>()
    private val fakeProviders = mutableMapOf<String, String>()

    private val syncStatus = MutableStateFlow<SyncStatus>(SyncStatus.Idle)

    fun setFakeRate(fromCurrency: String, toCurrency: String, rate: BigDecimal) {
        fakeRates[Pair(fromCurrency, toCurrency)] = rate
    }

    fun setFakeProvider(providerId: String, providerName: String) {
        fakeProviders[providerId] = providerName
    }

    override suspend fun convert(
        providerId: String,
        fromCurrencyCode: String,
        toCurrencyCode: String,
        amount: BigDecimal
    ): Result<BigDecimal> {
        if (fromCurrencyCode == toCurrencyCode) return Result.success(amount)
        val rate =
            fakeRates[Pair(fromCurrencyCode, toCurrencyCode)] ?: BigDecimal("1.0") // default 1:1
        return if (fakeProviders[providerId] == "provider1") Result.success(amount * rate) else Result.failure(
            Exception(PROVIDER_ERROR)
        )
    }

    override suspend fun getProviders(): Result<List<Pair<String, String>>> {
        return Result.success(
            fakeProviders.map { (id, name) -> Pair(id, name) }
        )
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

    companion object {
        const val PROVIDER_ERROR = "this provider does not provide this currency"
    }
}
