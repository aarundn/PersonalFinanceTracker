package com.example.domain.di

import com.example.domain.usecase.budget_usecases.AddBudgetUseCase
import com.example.domain.usecase.transaction_usecases.AddTransactionUseCase
import com.example.domain.usecase.budget_usecases.DeleteBudgetUseCase
import com.example.domain.usecase.transaction_usecases.DeleteTransactionUseCase
import com.example.domain.usecase.budget_usecases.GetBudgetByIdUseCase
import com.example.domain.usecase.budget_usecases.GetBudgetTransactionsUseCase
import com.example.domain.usecase.budget_usecases.GetBudgetsUseCase
import com.example.domain.usecase.transaction_usecases.GetTransactionsUseCase
import com.example.domain.usecase.budget_usecases.UpdateBudgetUseCase
import com.example.domain.usecase.transaction_usecases.UpdateTransactionUseCase
import com.example.domain.usecase.ValidateInputsUseCase
import com.example.domain.usecase.transaction_usecases.GetTransactionByIdUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::AddTransactionUseCase)
    factoryOf(::DeleteTransactionUseCase)
    factoryOf(::GetTransactionsUseCase)
    factoryOf(::UpdateTransactionUseCase)
    factoryOf(::GetTransactionByIdUseCase)

    factoryOf(::AddBudgetUseCase)
    factoryOf(::UpdateBudgetUseCase)
    factoryOf(::DeleteBudgetUseCase)
    factoryOf(::GetBudgetsUseCase)
    factoryOf(::GetBudgetByIdUseCase)
    factoryOf(::GetBudgetTransactionsUseCase)

    singleOf(::ValidateInputsUseCase)
}
