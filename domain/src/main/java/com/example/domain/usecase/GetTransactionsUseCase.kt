package com.example.domain.usecase

import com.example.domain.repo.TransactionRepository

class GetTransactionsUseCase(
    private val transactionRepository: TransactionRepository

) {
     operator fun invoke() = transactionRepository.getAllTransactions()

}