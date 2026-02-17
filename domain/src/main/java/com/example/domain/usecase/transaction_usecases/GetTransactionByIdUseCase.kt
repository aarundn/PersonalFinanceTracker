package com.example.domain.usecase.transaction_usecases

import com.example.domain.repo.TransactionRepository

class GetTransactionByIdUseCase(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transactionId: String) = runCatching {
        repository.getTransactionById(transactionId)
    }
}