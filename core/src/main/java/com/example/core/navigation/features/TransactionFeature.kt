package com.example.core.navigation.features

import com.example.core.navigation.Feature

interface TransactionFeature: Feature {
    fun transactionsRoute(): Any
    fun addTransactionRoute(): Any
    fun editTransactionRoute(transactionId: String): Any
}