package com.example.personalfinancetracker

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.example.conversion_rate.di.conversionRateModule
import com.example.data.di.dataModule
import com.example.domain.di.domainModule
import com.example.personalfinancetracker.features.budget.di.budgetModule
import com.example.personalfinancetracker.features.transaction.di.transactionsModule
import homeModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import settingsModule

class App : Application(), Configuration.Provider {


    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            workManagerFactory()
            modules(appModule)
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()

}

val mainModule = module {
    viewModelOf( ::MainViewModel)
}

val appModule = listOf(
    dataModule,
    domainModule,
    conversionRateModule,
    homeModule,
    budgetModule,
    transactionsModule,
    settingsModule,
    mainModule,
)