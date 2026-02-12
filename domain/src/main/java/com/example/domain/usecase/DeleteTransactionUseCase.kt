package com.example.domain.usecase

import com.example.domain.repo.TransactionRepository

class DeleteTransactionUseCase(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(id: String) = runCatching {
        transactionRepository.deleteTransactionById(id)
    }
}
