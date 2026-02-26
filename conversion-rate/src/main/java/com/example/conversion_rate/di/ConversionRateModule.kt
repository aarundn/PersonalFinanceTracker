package com.example.conversion_rate.di

import androidx.room.Room
import androidx.work.WorkManager
import com.example.conversion_rate.data.local.ConversionRateDatabase
import com.example.conversion_rate.data.repository.ExchangeRateRepositoryImpl
import com.example.conversion_rate.domain.repository.ExchangeRateRepository
import com.example.conversion_rate.domain.usecase.ConvertCurrencyUseCase
import com.example.conversion_rate.domain.usecase.GetProvidersUseCase
import com.example.conversion_rate.domain.usecase.InitializeRateSyncUseCase
import com.example.conversion_rate.domain.usecase.SyncExchangeRatesUseCase
import com.example.conversion_rate.sync.RateSyncManager
import com.example.conversion_rate.sync.RateSyncScheduler
import com.example.conversion_rate.sync.RateSyncWorker
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val conversionRateModule = module {

    // Data

    single<ConversionRateDatabase> {
        Room.databaseBuilder(
            androidContext(),
            ConversionRateDatabase::class.java,
            ConversionRateDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration(false).build()
    }

    single { get<ConversionRateDatabase>().conversionRateDao() }

    // Repository
    // ExchangeRateProviderPort must be provided by the data module (adapters)

    single<ExchangeRateRepository> {
        ExchangeRateRepositoryImpl(
            providers = getAll(),
            dao = get(),
            syncManager = get()
        )
    }

    // Use Cases

    factoryOf(::ConvertCurrencyUseCase)
    factoryOf(::GetProvidersUseCase)
    factoryOf(::SyncExchangeRatesUseCase)
    factoryOf(::InitializeRateSyncUseCase)


    // Sync
    single<RateSyncManager> { RateSyncScheduler(WorkManager.getInstance(androidContext())) }
    workerOf(::RateSyncWorker)
}
