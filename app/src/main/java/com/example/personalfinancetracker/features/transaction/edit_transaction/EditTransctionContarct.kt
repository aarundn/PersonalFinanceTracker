package com.example.personalfinancetracker.features.transaction.edit_transaction

import androidx.compose.runtime.Immutable
import com.example.core.common.MVIState
import com.example.core.common.MVIUiEvent
import com.example.core.common.MVIUiSideEffect
import com.example.core.model.Category
import com.example.core.model.Currency
import com.example.personalfinancetracker.features.transaction.model.TransactionUi
import com.example.core.common.UiText

@Immutable
data class EditTransactionState(
    val transaction: TransactionUi? = null,
    val isIncome: Boolean = false,
    val selectedCategory: Category? = null,
    val amount: String = "",
    val selectedCurrency: Currency? = null,
    val date: Long = 0L,
    val notes: String = "",
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val isEditing: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
    val categories: List<Category> = emptyList(),
    val showDatePicker: Boolean = false,
) : MVIState

sealed class EditTransactionEvent : MVIUiEvent {
    data class OnTransactionTypeChanged(val isIncome: Boolean) : EditTransactionEvent()
    data class OnCategoryChanged(val category: Category) : EditTransactionEvent()
    data class OnAmountChanged(val amount: String) : EditTransactionEvent()
    data class OnCurrencyChanged(val currency: Currency) : EditTransactionEvent()
    data class OnDateChanged(val date: Long) : EditTransactionEvent()
    data class OnNotesChanged(val notes: String) : EditTransactionEvent()
    object OnShowDatePicker : EditTransactionEvent()
    object OnHideDatePicker : EditTransactionEvent()
    object OnSave : EditTransactionEvent()
    object OnCancel : EditTransactionEvent()
    object OnDelete : EditTransactionEvent()
    object OnDismissDelete : EditTransactionEvent()
    object OnConfirmDelete : EditTransactionEvent()
    object OnEdit : EditTransactionEvent()
}

sealed class EditTransactionSideEffect : MVIUiSideEffect {
    object NavigateBack : EditTransactionSideEffect()
    object NavigateToTransactions : EditTransactionSideEffect()
    data class ShowError(val message: UiText) : EditTransactionSideEffect()
    data class ShowSuccess(val message: UiText) : EditTransactionSideEffect()
}