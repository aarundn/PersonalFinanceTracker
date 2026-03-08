package com.example.domain.usecase

import com.example.domain.ValidationResult
import com.example.domain.util.DomainConstants

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
            return ValidationResult.Error(DomainConstants.ERROR_CATEGORY_REQUIRED)
        }

        if (currency.isBlank()) {
            return ValidationResult.Error(DomainConstants.ERROR_CURRENCY_REQUIRED)
        }

        if (amount.isBlank()) {
            return ValidationResult.Error(DomainConstants.ERROR_AMOUNT_REQUIRED)
        }

        val amountValue = amount.toDoubleOrNull()
        if (amountValue == null || amountValue <= 0) {
            return ValidationResult.Error(DomainConstants.ERROR_INVALID_AMOUNT)
        }

        if (date <= 0) {
            return ValidationResult.Error(DomainConstants.ERROR_DATE_REQUIRED)
        }

        return ValidationResult.Success
    }

}
