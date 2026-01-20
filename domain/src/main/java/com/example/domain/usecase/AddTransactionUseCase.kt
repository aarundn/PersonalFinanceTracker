package com.example.domain.usecase

import com.example.domain.model.Transaction
import com.example.domain.repo.TransactionRepository

class AddTransactionUseCase(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) =
        transactionRepository.addTransaction(transaction)

}