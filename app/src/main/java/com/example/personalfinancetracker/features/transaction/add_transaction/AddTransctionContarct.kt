package com.example.personalfinancetracker.features.transaction.add_transaction

import androidx.compose.runtime.Immutable
import com.example.core.common.MVIState
import com.example.core.common.MVIUiEvent
import com.example.core.common.MVIUiSideEffect
import com.example.core.model.Category

@Immutable
data class AddTransactionState(
    val isIncome: Boolean = false,
    val title: String = "",
    val category: String = "",
    val amount: String = "",
    val currency: String = "",
    val date: Long = System.currentTimeMillis(),
    val notes: String = "",
    val convertedAmount: Double? = null,
    val categories: List<Category> = emptyList(),
    val isConverting: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
) : MVIState

sealed class AddTransactionEvent : MVIUiEvent {
    data class OnTransactionTypeChanged(val isIncome: Boolean) : AddTransactionEvent()
    data class OnTitleChanged(val title: String) : AddTransactionEvent()
    data class OnCategoryChanged(val category: String) : AddTransactionEvent()
    data class OnAmountChanged(val amount: String) : AddTransactionEvent()
    data class OnCurrencyChanged(val currency: String) : AddTransactionEvent()
    data class OnDateChanged(val date: Long) : AddTransactionEvent()
    data class OnNotesChanged(val notes: String) : AddTransactionEvent()
    object OnSave : AddTransactionEvent()
    object OnCancel : AddTransactionEvent()
}

sealed class AddTransactionSideEffect : MVIUiSideEffect {
    object NavigateBack : AddTransactionSideEffect()
    object NavigateToTransactions : AddTransactionSideEffect()
    data class ShowError(val message: String) : AddTransactionSideEffect()
    data class ShowSuccess(val message: String) : AddTransactionSideEffect()
}
