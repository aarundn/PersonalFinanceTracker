package com.example.personalfinancetracker.features.transaction.add_transaction

object AddTransactionContract {

    data class State(
        val isIncome: Boolean = false,
        val title: String = "",
        val category: String = "",
        val amount: String = "",
        val currency: String = "",
        val date: Long = 0L,
        val notes: String = "",
        val convertedAmount: Double? = null,
        val isConverting: Boolean = false,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed class Event {
        data class OnTransactionTypeChanged(val isIncome: Boolean) : Event()
        data class OnTitleChanged(val title: String) : Event()
        data class OnCategoryChanged(val category: String) : Event()
        data class OnAmountChanged(val amount: String) : Event()
        data class OnCurrencyChanged(val currency: String) : Event()
        data class OnDateChanged(val date: String) : Event()
        data class OnNotesChanged(val notes: String) : Event()
        object OnConvertCurrency : Event()
        object OnSave : Event()
        object OnCancel : Event()
    }

    sealed class SideEffect {
        object NavigateBack : SideEffect()
        object NavigateToTransactions : SideEffect()
        data class ShowError(val message: String) : SideEffect()
        data class ShowSuccess(val message: String) : SideEffect()
    }
}
