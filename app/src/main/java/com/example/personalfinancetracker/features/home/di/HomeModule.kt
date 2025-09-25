package com.example.personalfinancetracker.features.home.di

import com.example.core.navigation.features.HomeFeature
import com.example.personalfinancetracker.features.home.HomeRepository
import com.example.personalfinancetracker.features.home.InMemoryHomeRepository
import com.example.personalfinancetracker.features.home.HomeViewModel
import com.example.personalfinancetracker.features.home.navigation.HomeFeatureImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val homeModule = module {
    singleOf(::HomeFeatureImpl) bind HomeFeature::class
    singleOf(::InMemoryHomeRepository) bind HomeRepository::class
    viewModel { HomeViewModel(repository = get()) }
}