package com.example.domain.usecase

import com.example.domain.ValidationResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ValidateTransactionInputsUseCaseTest {

    private lateinit var validateInputs: ValidateTransactionInputsUseCase

    @Before
    fun setUp() {
        validateInputs = ValidateTransactionInputsUseCase()
    }

    @Test
    fun `given valid inputs, should return Success`() {
        val result = validateInputs(
            category = "food",
            amount = "100.0",
            currency = "USD",
            date = System.currentTimeMillis()
        )

        assertTrue(result is ValidationResult.Success)
    }

    @Test
    fun `given empty category, should return Error with correct message`() {
        val result = validateInputs(
            category = "",
            amount = "100.0",
            currency = "USD",
            date = System.currentTimeMillis()
        )

        assertTrue(result is ValidationResult.Error)
        assertEquals("Category is required", (result as ValidationResult.Error).message)
    }

    @Test
    fun `given blank category with spaces, should return Error`() {
        val result = validateInputs(
            category = "   ",
            amount = "100.0",
            currency = "USD",
            date = System.currentTimeMillis()
        )

        assertTrue(result is ValidationResult.Error)
        assertEquals("Category is required", (result as ValidationResult.Error).message)
    }

    @Test
    fun `given empty amount, should return Error`() {
        val result = validateInputs(
            category = "food",
            amount = "",
            currency = "USD",
            date = System.currentTimeMillis()
        )

        assertTrue(result is ValidationResult.Error)
        assertEquals("Amount is required", (result as ValidationResult.Error).message)
    }

    @Test
    fun `given non-numeric amount, should return Error`() {
        val result = validateInputs(
            category = "food",
            amount = "abc",
            currency = "USD",
            date = System.currentTimeMillis()
        )

        assertTrue(result is ValidationResult.Error)
        assertEquals("Please enter a valid amount", (result as ValidationResult.Error).message)
    }

    @Test
    fun `given zero amount, should return Error`() {
        val result = validateInputs(
            category = "food",
            amount = "0",
            currency = "USD",
            date = System.currentTimeMillis()
        )

        assertTrue(result is ValidationResult.Error)
        assertEquals("Please enter a valid amount", (result as ValidationResult.Error).message)
    }

    @Test
    fun `given negative amount, should return Error`() {
        val result = validateInputs(
            category = "food",
            amount = "-50.0",
            currency = "USD",
            date = System.currentTimeMillis()
        )

        assertTrue(result is ValidationResult.Error)
        assertEquals("Please enter a valid amount", (result as ValidationResult.Error).message)
    }

    @Test
    fun `given empty currency, should return Error`() {
        val result = validateInputs(
            category = "food",
            amount = "100.0",
            currency = "",
            date = System.currentTimeMillis()
        )

        assertTrue(result is ValidationResult.Error)
        assertEquals("Currency is required", (result as ValidationResult.Error).message)
    }

    @Test
    fun `given zero date, should return Error`() {
        val result = validateInputs(
            category = "food",
            amount = "100.0",
            currency = "USD",
            date = 0L
        )

        assertTrue(result is ValidationResult.Error)
        assertEquals("Date is required", (result as ValidationResult.Error).message)
    }

    @Test
    fun `given negative date, should return Error`() {
        val result = validateInputs(
            category = "food",
            amount = "100.0",
            currency = "USD",
            date = -1L
        )

        assertTrue(result is ValidationResult.Error)
        assertEquals("Date is required", (result as ValidationResult.Error).message)
    }


    @Test
    fun `given multiple invalid fields, should return the FIRST error (category)`() {
        val result = validateInputs(
            category = "",
            amount = "",
            currency = "",
            date = 0L
        )

        assertTrue(result is ValidationResult.Error)
        assertEquals("Category is required", (result as ValidationResult.Error).message)
    }
}
