package com.example.personalfinancetracker.features.transaction.di

import com.example.core.navigation.Feature
import com.example.core.navigation.features.TransactionFeature
import com.example.personalfinancetracker.features.transaction.add_transaction.AddTransactionViewModel
import com.example.personalfinancetracker.features.transaction.edit_transaction.EditTransactionViewModel
import com.example.personalfinancetracker.features.transaction.navigation.TransactionFeatureImpl
import com.example.personalfinancetracker.features.transaction.transactions.TransactionsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.binds
import org.koin.dsl.module

val transactionsModule = module {
    singleOf(::TransactionFeatureImpl)  binds arrayOf(TransactionFeature::class, Feature::class)
    viewModelOf(::TransactionsViewModel)
    viewModelOf(::AddTransactionViewModel)
    viewModelOf(::EditTransactionViewModel)
}