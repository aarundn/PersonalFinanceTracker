package com.example.personalfinancetracker.features.transaction.di

import com.example.core.navigation.features.TransactionFeature
import com.example.personalfinancetracker.features.transaction.navigation.TransactionFeatureImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val transactionsModule = module {
    singleOf(::TransactionFeatureImpl) bind TransactionFeature::class

}