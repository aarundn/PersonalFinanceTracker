package com.example.domain.di

import com.example.domain.usecase.AddTransactionUseCase
import com.example.domain.usecase.GetTransactionsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::AddTransactionUseCase)
    factoryOf(::GetTransactionsUseCase)
}