package com.example.personalfinancetracker

import android.app.Application
import com.example.conversion_rate.di.conversionRateModule
import com.example.data.di.dataModule
import com.example.domain.di.domainModule
import com.example.personalfinancetracker.features.budget.di.budgetModule
import com.example.personalfinancetracker.features.transaction.di.transactionsModule
import homeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import settingsModule

class App: Application()  {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}

val appModule = listOf(
    dataModule,
    domainModule,
    conversionRateModule,
    homeModule,
    budgetModule,
    transactionsModule,
    settingsModule,
)