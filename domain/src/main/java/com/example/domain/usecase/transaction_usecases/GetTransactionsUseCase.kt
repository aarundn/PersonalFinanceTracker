package com.example.domain.usecase.transaction_usecases

import com.example.domain.repo.TransactionRepository

class GetTransactionsUseCase(
    private val transactionRepository: TransactionRepository

) {
     operator fun invoke() = transactionRepository.getAllTransactions()

}