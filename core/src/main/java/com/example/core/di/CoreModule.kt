package com.example.core.di

import kotlinx.datetime.Clock
import org.koin.dsl.module

val coreModule = module {
    single<Clock> { Clock.System }
}
