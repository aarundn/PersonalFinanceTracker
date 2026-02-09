package com.example.domain.usecase

import com.example.domain.ValidationResult

class ValidateTransactionInputsUseCase {

    /**
     * Validates all transaction fields and returns a ValidationResult.
     */
    operator fun invoke(
        category: String,
        amount: String,
        currency: String,
        date: Long
    ): ValidationResult {
        if (category.isBlank()) {
            return ValidationResult.Error("Category is required")
        }

        if (amount.isBlank()) {
            return ValidationResult.Error("Amount is required")
        }
        val amountValue = amount.toDoubleOrNull()
        if (amountValue == null || amountValue <= 0) {
            return ValidationResult.Error("Please enter a valid amount")
        }

        if (currency.isBlank()) {
            return ValidationResult.Error("Currency is required")
        }

        if (date <= 0) {
            return ValidationResult.Error("Date is required")
        }
        
        return ValidationResult.Success
    }

}
