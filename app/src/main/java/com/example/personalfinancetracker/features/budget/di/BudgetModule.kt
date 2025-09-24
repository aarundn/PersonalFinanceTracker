package com.example.personalfinancetracker.features.budget.di

import com.example.core.navigation.features.BudgetFeature
import com.example.personalfinancetracker.features.budget.navigation.BudgetFeatureImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val budgetModule = module {
    singleOf(::BudgetFeatureImpl) bind BudgetFeature::class
}