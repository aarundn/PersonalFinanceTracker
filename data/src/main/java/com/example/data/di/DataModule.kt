package com.example.data.di

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.conversion_rate.domain.port.ExchangeRateProviderPort
import com.example.data.local.TrackerDatabase
import com.example.data.remote.budget.RemoteBudgetRepo
import com.example.data.remote.budget.RemoteBudgetRepoImpl
import com.example.data.remote.exchangerate.ExchangeRateApiAdapter
import com.example.data.remote.exchangerate.ExchangeRateApiService
import com.example.data.remote.frankfurter.FrankfurterAdapter
import com.example.data.remote.frankfurter.FrankfurterApiService
import com.example.data.remote.transaction.RemoteTransactionRepo
import com.example.data.remote.transaction.RemoteTransactionRepoImpl
import com.example.data.repository.BudgetRepositoryImp
import com.example.data.repository.TransactionRepositoryImp
import com.example.data.repository.UserPreferencesRepositoryImpl
import com.example.data.sync.DataSyncManager
import com.example.data.sync.DataSyncScheduler
import com.example.data.sync.DataSyncWorker
import com.example.domain.repo.BudgetRepository
import com.example.domain.repo.TransactionRepository
import com.example.domain.repo.UserPreferencesRepository
import androidx.work.WorkManager
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val MIGRATION_11_12 = object : Migration(11, 12) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE transactions ADD COLUMN syncStatus TEXT NOT NULL DEFAULT 'PENDING'")
        db.execSQL("ALTER TABLE budgets ADD COLUMN syncStatus TEXT NOT NULL DEFAULT 'PENDING'")
    }
}

val dataModule = module {

    // Network

    single {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    }
                )
            }
            install(Logging) {
                level = LogLevel.BODY
            }
        }
    }

    // Database

    single<FirebaseFirestore> { Firebase.firestore }
    single<TrackerDatabase> {
        Room.databaseBuilder(
            androidContext(),
            TrackerDatabase::class.java,
            TrackerDatabase.DATABASE_NAME
        )
            .addMigrations(MIGRATION_11_12)
            .fallbackToDestructiveMigration(false)
            .build()
    }

    factory { get<TrackerDatabase>().transactionDao() }
    factory { get<TrackerDatabase>().budgetDao() }
    // Repositories

    singleOf(::TransactionRepositoryImp) { bind<TransactionRepository>() }
    singleOf(::BudgetRepositoryImp) { bind<BudgetRepository>() }
    singleOf(::UserPreferencesRepositoryImpl) { bind<UserPreferencesRepository>() }
    singleOf(::RemoteTransactionRepoImpl) { bind<RemoteTransactionRepo>() }
    singleOf(::RemoteBudgetRepoImpl) { bind<RemoteBudgetRepo>() }

    //  Exchange Rate Providers

    singleOf(::FrankfurterApiService)
    single<ExchangeRateProviderPort>(named(FrankfurterAdapter.PROVIDER_ID)) { FrankfurterAdapter(get()) }

    singleOf(::ExchangeRateApiService)
    single<ExchangeRateProviderPort>(named(ExchangeRateApiAdapter.PROVIDER_ID)) {
        ExchangeRateApiAdapter(
            get()
        )
    }

    // Sync
    single { WorkManager.getInstance(androidContext()) }
    workerOf(::DataSyncWorker)
    singleOf(::DataSyncScheduler) { bind<DataSyncManager>() }

}

