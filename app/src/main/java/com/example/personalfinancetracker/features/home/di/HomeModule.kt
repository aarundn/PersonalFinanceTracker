package com.example.personalfinancetracker.features.home.di

import com.example.core.navigation.features.HomeFeature
import com.example.personalfinancetracker.features.home.HomeViewModel
import com.example.personalfinancetracker.features.home.navigation.HomeFeatureImpl
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val homeModule = module {
    singleOf(::HomeFeatureImpl) bind HomeFeature::class
    viewModelOf(::HomeViewModel)
}