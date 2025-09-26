package com.example.personalfinancetracker.features.transaction.add_transaction

object AddTransactionContract {

    data class State(
        val isIncome: Boolean = false,
        val title: String = "",
        val category: String = "",
        val amount: String = "",
        val currency: String = "USD",
        val date: String = "",
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

    data class TransactionData(
        val isIncome: Boolean,
        val title: String,
        val category: String,
        val amount: Double,
        val originalAmount: Double,
        val currency: String,
        val baseCurrency: String,
        val date: String,
        val notes: String
    )

    object Categories {
        val incomeCategories = listOf("Salary", "Freelance", "Investment", "Gift", "Other")
        val expenseCategories = listOf("Food", "Transport", "Shopping", "Bills", "Entertainment", "Health", "Other")
    }

    object PopularCurrencies {
        val currencies = listOf(
            CurrencyInfo("USD", "$", "US Dollar"),
            CurrencyInfo("EUR", "€", "Euro"),
            CurrencyInfo("GBP", "£", "British Pound"),
            CurrencyInfo("JPY", "¥", "Japanese Yen"),
            CurrencyInfo("CAD", "C$", "Canadian Dollar"),
            CurrencyInfo("AUD", "A$", "Australian Dollar"),
            CurrencyInfo("CHF", "CHF", "Swiss Franc"),
            CurrencyInfo("CNY", "¥", "Chinese Yuan"),
            CurrencyInfo("INR", "₹", "Indian Rupee"),
            CurrencyInfo("BRL", "R$", "Brazilian Real")
        )
    }

    data class CurrencyInfo(
        val code: String,
        val symbol: String,
        val name: String
    )
}