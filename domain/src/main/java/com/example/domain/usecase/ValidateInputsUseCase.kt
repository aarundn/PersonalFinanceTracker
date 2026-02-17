package com.example.domain.usecase

import com.example.domain.ValidationResult

class ValidateInputsUseCase {

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

        if (currency.isBlank()) {
            return ValidationResult.Error("Currency is required")
        }

        if (amount.isBlank()) {
            return ValidationResult.Error("Amount is required")
        }

        val amountValue = amount.toDoubleOrNull()
        if (amountValue == null || amountValue <= 0) {
            return ValidationResult.Error("Please enter a valid amount")
        }

        if (date <= 0) {
            return ValidationResult.Error("Date is required")
        }

        return ValidationResult.Success
    }

}
