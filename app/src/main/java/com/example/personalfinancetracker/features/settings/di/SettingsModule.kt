package com.example.personalfinancetracker.features.settings.di

import com.example.personalfinancetracker.features.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsModule = module {
    viewModelOf(::SettingsViewModel)
}
