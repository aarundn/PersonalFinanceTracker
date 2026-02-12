package com.example.domain.usecase

import com.example.domain.model.Transaction
import com.example.domain.repo.TransactionRepository

class UpdateTransactionUseCase(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) = runCatching {
        transactionRepository.updateTransaction(transaction)
    }
}
