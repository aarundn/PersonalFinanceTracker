package com.example.domain.usecase.transaction_usecases

import com.example.domain.model.Transaction
import com.example.domain.repo.TransactionRepository

class AddTransactionUseCase(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) = runCatching {
        transactionRepository.addTransaction(transaction)
    }
}