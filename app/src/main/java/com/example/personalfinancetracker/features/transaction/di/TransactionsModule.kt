package com.example.personalfinancetracker.features.transaction.di

import com.example.core.navigation.features.TransactionFeature
import com.example.personalfinancetracker.features.transaction.add_transaction.AddTransactionViewModel
import com.example.personalfinancetracker.features.transaction.navigation.TransactionFeatureImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val transactionsModule = module {
    singleOf(::TransactionFeatureImpl) bind TransactionFeature::class
    
    // AddTransaction ViewModel
    viewModel { AddTransactionViewModel() }
}