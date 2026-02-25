package com.example.conversion_rate.di

import androidx.room.Room
import com.example.conversion_rate.data.local.ConversionRateDatabase
import com.example.conversion_rate.data.repository.ExchangeRateRepositoryImpl
import com.example.conversion_rate.domain.repository.ExchangeRateRepository
import com.example.conversion_rate.domain.usecase.ConvertCurrencyUseCase
import com.example.conversion_rate.domain.usecase.GetProvidersUseCase
import org.koin.android.ext.koin.androidContext
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
    // ExchangeRateProviderPort must be provided by the app module (adapter)

    single<ExchangeRateRepository> {
        ExchangeRateRepositoryImpl(
            providers = getAll(),  // collects all ExchangeRateProviderPort bindings from app module
            dao = get(),
        )
    }

    // Use Cases

    factoryOf(::ConvertCurrencyUseCase)
    factoryOf(::GetProvidersUseCase)
}

