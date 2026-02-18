package com.example.personalfinancetracker.features.budget.edit_budget

import androidx.compose.runtime.Immutable
import com.example.core.common.MVIState
import com.example.core.common.MVIUiEvent
import com.example.core.common.MVIUiSideEffect
import com.example.core.model.Category
import com.example.core.model.Currency
import com.example.domain.model.BudgetPeriod
import com.example.personalfinancetracker.features.budget.model.BudgetInsightsUi
import com.example.personalfinancetracker.features.budget.model.BudgetUi

@Immutable
data class EditBudgetState(
    val budget: BudgetUi? = null,
    val selectedCategory: Category? = null,
    val amountInput: String = "",
    val periodInput: BudgetPeriod = BudgetPeriod.default,
    val notesInput: String = "",
    val selectedCurrency: Currency? = null,
    val isEditing: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDeleteConfirmation: Boolean = false,
    val categories: List<Category> = emptyList(),
    val insights: BudgetInsightsUi? = null
) : MVIState

sealed class EditBudgetEvent : MVIUiEvent {
    data class OnCategoryChanged(val category: Category) : EditBudgetEvent()
    data class OnAmountChanged(val amount: String) : EditBudgetEvent()
    data class OnCurrencyChanged(val currency: Currency) : EditBudgetEvent()
    data class OnPeriodChanged(val period: BudgetPeriod) : EditBudgetEvent()
    data class OnNotesChanged(val notes: String) : EditBudgetEvent()
    data object OnSave : EditBudgetEvent()
    data object OnCancel : EditBudgetEvent()
    data object OnEdit : EditBudgetEvent()
    data object OnDelete : EditBudgetEvent()
    data object OnConfirmDelete : EditBudgetEvent()
    data object OnDismissDelete : EditBudgetEvent()
}

sealed class EditBudgetSideEffect : MVIUiSideEffect {
    data object NavigateBack : EditBudgetSideEffect()
    data class ShowSuccess(val message: String) : EditBudgetSideEffect()
    data class ShowError(val message: String) : EditBudgetSideEffect()
}
