package com.example.personalfinancetracker.features.transaction.edit_transaction

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.core.model.Categories

object EditTransactionContract {

    @Immutable
    data class State(
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
        val categories : List<Categories> = emptyList(),
    )

    sealed class Event {
        data class OnTransactionTypeChanged(val isIncome: Boolean) : Event()
        data class OnTitleChanged(val title: String) : Event()
        data class OnCategoryChanged(val category: String) : Event()
        data class OnAmountChanged(val amount: String) : Event()
        data class OnCurrencyChanged(val currency: String) : Event()
        data class OnDateChanged(val date: Long) : Event()
        data class OnNotesChanged(val notes: String) : Event()
        data class OnLocationChanged(val location: String) : Event()
        data class OnPaymentMethodChanged(val paymentMethod: String) : Event()
        object OnConvertCurrency : Event()
        object OnSave : Event()
        object OnCancel : Event()
        object OnDelete : Event()
        object OnEdit : Event()
        object OnLoadTransaction : Event()
    }

    sealed class SideEffect {
        object NavigateBack : SideEffect()
        object NavigateToTransactions : SideEffect()
        data class ShowError(val message: String) : SideEffect()
        data class ShowSuccess(val message: String) : SideEffect()
        data class ShowDeleteConfirmation(val transactionId: String) : SideEffect()
    }
}