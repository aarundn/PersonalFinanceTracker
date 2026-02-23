package com.example.personalfinancetracker.features.budget.di

import com.example.core.navigation.Feature
import com.example.core.navigation.features.BudgetFeature
import com.example.personalfinancetracker.features.budget.add_budget.AddBudgetViewModel
import com.example.personalfinancetracker.features.budget.budgets.BudgetViewModel
import com.example.personalfinancetracker.features.budget.edit_budget.EditBudgetViewModel
import com.example.personalfinancetracker.features.budget.navigation.BudgetFeatureImpl
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.binds
import org.koin.dsl.module

val budgetModule = module {
    singleOf(::BudgetFeatureImpl) binds arrayOf(BudgetFeature::class ,Feature::class)

    // view models
    viewModelOf(::BudgetViewModel)
    viewModelOf(::AddBudgetViewModel)
    viewModelOf(::EditBudgetViewModel)
}
