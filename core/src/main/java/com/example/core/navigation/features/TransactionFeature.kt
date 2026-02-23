package com.example.core.navigation.features

import com.example.core.navigation.AppRoutes
import com.example.core.navigation.Feature

interface TransactionFeature: Feature {
    fun transactionsRoute(): AppRoutes
    fun addTransactionRoute(): AppRoutes
    fun editTransactionRoute(transactionId: String): AppRoutes
}