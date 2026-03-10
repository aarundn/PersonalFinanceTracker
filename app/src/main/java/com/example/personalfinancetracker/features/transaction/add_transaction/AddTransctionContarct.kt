package com.example.personalfinancetracker.features.transaction.add_transaction

import androidx.compose.runtime.Immutable
import com.example.core.common.MVIState
import com.example.core.common.MVIUiEvent
import com.example.core.common.MVIUiSideEffect
import com.example.core.model.Category
import com.example.core.model.Currency
import com.example.personalfinancetracker.features.budget.model.BudgetUi
import com.example.core.common.UiText

@Immutable
data class AddTransactionState(
    val isIncome: Boolean = false,
    val selectedCategory: Category? = null,
    val amount: String = "",
    val selectedCurrency: Currency? = null,
    val date: Long = System.currentTimeMillis(),
    val notes: String = "",
    val convertedAmount: Double? = null,
    val categories: List<Category> = emptyList(),
    val isConverting: Boolean = false,
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val availableBudgets: List<BudgetUi> = emptyList(),
    val selectedBudgetId: String? = null,
    val showBudgetSelector: Boolean = false,
    val showDatePicker: Boolean = false,
) : MVIState

sealed class AddTransactionEvent : MVIUiEvent {
    data class OnTransactionTypeChanged(val isIncome: Boolean) : AddTransactionEvent()
    data class OnCategoryChanged(val category: Category) : AddTransactionEvent()
    data class OnAmountChanged(val amount: String) : AddTransactionEvent()
    data class OnCurrencyChanged(val currency: Currency) : AddTransactionEvent()
    data class OnDateChanged(val date: Long) : AddTransactionEvent()
    data class OnNotesChanged(val notes: String) : AddTransactionEvent()
    data class OnBudgetSelected(val budgetId: String) : AddTransactionEvent()
    object OnAddBudgetClicked : AddTransactionEvent()
    object OnShowBudgetSelector : AddTransactionEvent()
    object OnHideBudgetSelector : AddTransactionEvent()
    object OnShowDatePicker : AddTransactionEvent()
    object OnHideDatePicker : AddTransactionEvent()
    object OnSave : AddTransactionEvent()
    object OnCancel : AddTransactionEvent()
}

sealed class AddTransactionSideEffect : MVIUiSideEffect {
    object NavigateBack : AddTransactionSideEffect()
    object NavigateToTransactions : AddTransactionSideEffect()
    object NavigateToAddBudget : AddTransactionSideEffect()
    data class ShowError(val message: UiText) : AddTransactionSideEffect()
    data class ShowSuccess(val message: UiText) : AddTransactionSideEffect()
}
