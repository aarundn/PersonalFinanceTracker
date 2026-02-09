package com.example.personalfinancetracker.features.transaction.di

import com.example.core.navigation.features.TransactionFeature
import com.example.personalfinancetracker.features.transaction.add_transaction.AddTransactionViewModel
import com.example.personalfinancetracker.features.transaction.edit_transaction.EditTransactionViewModel
import com.example.personalfinancetracker.features.transaction.navigation.TransactionFeatureImpl
import com.example.personalfinancetracker.features.transaction.transactions.TransactionsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val transactionsModule = module {
    singleOf(::TransactionFeatureImpl) bind TransactionFeature::class

    // view models
    viewModelOf(::AddTransactionViewModel)
    viewModelOf(::TransactionsViewModel)
    viewModelOf(::EditTransactionViewModel)

}