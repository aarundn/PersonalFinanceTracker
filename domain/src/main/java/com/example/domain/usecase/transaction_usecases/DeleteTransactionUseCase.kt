package com.example.domain.usecase.transaction_usecases

import com.example.domain.repo.TransactionRepository

class DeleteTransactionUseCase(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(id: String) = runCatching {
        transactionRepository.deleteTransactionById(id)
    }
}
