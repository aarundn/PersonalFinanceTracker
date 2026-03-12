package com.example.personalfinancetracker.features.budget.add_budget

import androidx.compose.runtime.Immutable
import com.example.core.common.MVIState
import com.example.core.common.MVIUiEvent
import com.example.core.common.MVIUiSideEffect
import com.example.core.model.Category
import com.example.core.model.Currency
import com.example.domain.model.BudgetPeriod
import com.example.core.common.UiText

@Immutable
data class AddBudgetState(
    val selectedCategory: Category? = null,
    val amountInput: String = "",
    val selectedCurrency: Currency? = null,
    val period: BudgetPeriod = BudgetPeriod.default,
    val notes: String = "",
    val startDate: Long = System.currentTimeMillis(),
    val isSaving: Boolean = false,
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val showDatePicker: Boolean = false,
    val categories: List<Category> = emptyList()
) : MVIState

sealed class AddBudgetEvent : MVIUiEvent {
    data class OnCategorySelected(val category: Category) : AddBudgetEvent()
    data class OnAmountChanged(val amount: String) : AddBudgetEvent()
    data class OnCurrencyChanged(val currency: Currency) : AddBudgetEvent()
    data class OnPeriodChanged(val period: BudgetPeriod) : AddBudgetEvent()
    data class OnNotesChanged(val notes: String) : AddBudgetEvent()
    data class OnDateChanged(val date: Long) : AddBudgetEvent()
    data object OnShowDatePicker : AddBudgetEvent()
    data object OnHideDatePicker : AddBudgetEvent()
    data object OnSave : AddBudgetEvent()
    data object OnCancel : AddBudgetEvent()
}

sealed class AddBudgetSideEffect : MVIUiSideEffect {
    data object NavigateBack : AddBudgetSideEffect()
    data class ShowError(val message: UiText) : AddBudgetSideEffect()
    data class ShowSuccess(val message: UiText) : AddBudgetSideEffect()
}
