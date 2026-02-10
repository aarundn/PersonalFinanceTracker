package com.example.personalfinancetracker.features.transaction.edit_transaction

import androidx.compose.runtime.Immutable
import com.example.core.common.MVIState
import com.example.core.common.MVIUiEvent
import com.example.core.common.MVIUiSideEffect
import com.example.core.model.Category

@Immutable
data class EditTransactionState(
    val transactionId: String = "",
    val isIncome: Boolean = false,
    val selectedCategory: Category? = null,
    val amount: String = "",
    val currency: String = "USD",
    val date: Long = 0L,
    val notes: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEditing: Boolean = false,
    val categories: List<Category> = emptyList(),
) : MVIState

sealed class EditTransactionEvent : MVIUiEvent {
    data class OnTransactionTypeChanged(val isIncome: Boolean) : EditTransactionEvent()
    data class OnCategoryChanged(val category: Category) : EditTransactionEvent()
    data class OnAmountChanged(val amount: String) : EditTransactionEvent()
    data class OnCurrencyChanged(val currency: String) : EditTransactionEvent()
    data class OnDateChanged(val date: Long) : EditTransactionEvent()
    data class OnNotesChanged(val notes: String) : EditTransactionEvent()
    object OnSave : EditTransactionEvent()
    object OnCancel : EditTransactionEvent()
    object OnDelete : EditTransactionEvent()
    object OnEdit : EditTransactionEvent()
}

sealed class EditTransactionSideEffect : MVIUiSideEffect {
    object NavigateBack : EditTransactionSideEffect()
    object NavigateToTransactions : EditTransactionSideEffect()
    data class ShowError(val message: String) : EditTransactionSideEffect()
    data class ShowSuccess(val message: String) : EditTransactionSideEffect()
    data class ShowDeleteConfirmation(val transactionId: String) : EditTransactionSideEffect()
}