package com.example.domain

sealed interface ValidationResult {
    object Success : ValidationResult
    data class Error(val message: String) : ValidationResult
}