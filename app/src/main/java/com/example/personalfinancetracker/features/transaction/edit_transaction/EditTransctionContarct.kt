package com.example.personalfinancetracker.features.transaction.edit_transaction

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.core.common.MVIState
import com.example.core.common.MVIUiEvent
import com.example.core.common.MVIUiSideEffect
import com.example.core.model.Categories

@Immutable
data class EditTransactionState(
    val transactionId: String = "",
    val isIncome: Boolean = false,
    val title: String = "",
    val category: String = "",
    val amount: String = "",
    val currency: String = "USD",
    val date: Long = 0L,
    val notes: String = "",
    val location: String = "",
    val paymentMethod: String = "",
    val convertedAmount: Double? = null,
    val isConverting: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEditing: Boolean = false,
    val icon: ImageVector? = null,
    val iconTint: Color? = null,
    val categories: List<Categories> = emptyList(),
) : MVIState

sealed class EditTransactionEvent : MVIUiEvent {
    data class OnTransactionTypeChanged(val isIncome: Boolean) : EditTransactionEvent()
    data class OnTitleChanged(val title: String) : EditTransactionEvent()
    data class OnCategoryChanged(val category: String) : EditTransactionEvent()
    data class OnAmountChanged(val amount: String) : EditTransactionEvent()
    data class OnCurrencyChanged(val currency: String) : EditTransactionEvent()
    data class OnDateChanged(val date: Long) : EditTransactionEvent()
    data class OnNotesChanged(val notes: String) : EditTransactionEvent()
    data class OnLocationChanged(val location: String) : EditTransactionEvent()
    data class OnPaymentMethodChanged(val paymentMethod: String) : EditTransactionEvent()
    object OnSave : EditTransactionEvent()
    object OnCancel : EditTransactionEvent()
    object OnDelete : EditTransactionEvent()
    object OnEdit : EditTransactionEvent()
    object OnLoadTransaction : EditTransactionEvent()
}

sealed class EditTransactionSideEffect : MVIUiSideEffect {
    object NavigateBack : EditTransactionSideEffect()
    object NavigateToTransactions : EditTransactionSideEffect()
    data class ShowError(val message: String) : EditTransactionSideEffect()
    data class ShowSuccess(val message: String) : EditTransactionSideEffect()
    data class ShowDeleteConfirmation(val transactionId: String) : EditTransactionSideEffect()
}