package com.example.personalfinancetracker.features.transaction.add_transaction

import com.example.core.model.Categories

data class TransactionState(
    val isIncome: Boolean = false,
    val title: String = "",
    val category: String = "",
    val amount: String = "",
    val currency: String = "",
    val date: Long = System.currentTimeMillis(),
    val notes: String = "",
    val convertedAmount: Double? = null,
    val categories : List<Categories> = emptyList(),
    val isConverting: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
    )

    sealed class TransactionEvent {
        data class OnTransactionTypeChanged(val isIncome: Boolean) : TransactionEvent()
        data class OnTitleChanged(val title: String) : TransactionEvent()
        data class OnCategoryChanged(val category: String) : TransactionEvent()
        data class OnAmountChanged(val amount: String) : TransactionEvent()
        data class OnCurrencyChanged(val currency: String) : TransactionEvent()
        data class OnDateChanged(val date: String) : TransactionEvent()
        data class OnNotesChanged(val notes: String) : TransactionEvent()
        object OnConvertCurrency : TransactionEvent()
        object OnSave : TransactionEvent()
        object OnCancel : TransactionEvent()
    }

    sealed class TransactionSideEffect {
        object NavigateBack : TransactionSideEffect()
        object NavigateToTransactions : TransactionSideEffect()
        data class ShowError(val message: String) : TransactionSideEffect()
        data class ShowSuccess(val message: String) : TransactionSideEffect()
    }
