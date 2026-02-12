package com.example.domain.di

import com.example.domain.usecase.AddTransactionUseCase
import com.example.domain.usecase.GetTransactionsUseCase
import com.example.domain.usecase.UpdateTransactionUseCase
import com.example.domain.usecase.ValidateTransactionInputsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::AddTransactionUseCase)
    factoryOf(::GetTransactionsUseCase)
    factoryOf(::UpdateTransactionUseCase)
    
    // Singleton - stateless validation logic can be shared
    singleOf(::ValidateTransactionInputsUseCase)
}