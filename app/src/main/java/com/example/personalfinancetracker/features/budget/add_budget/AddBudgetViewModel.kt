package com.example.personalfinancetracker.features.budget.add_budget

import androidx.lifecycle.viewModelScope
import com.example.core.common.BaseViewModel
import com.example.core.model.Category
import com.example.core.model.DefaultCategories
import com.example.domain.model.Budget
import com.example.domain.usecase.budget_usecases.AddBudgetUseCase
import kotlinx.coroutines.launch
import java.util.UUID

class AddBudgetViewModel(
    private val addBudgetUseCase: AddBudgetUseCase
) : BaseViewModel<AddBudgetState, AddBudgetEvent, AddBudgetSideEffect>() {

    init {
        refreshCategories()
    }

    override fun createInitialState() = AddBudgetState()

    private fun refreshCategories() {
        setState { copy(categories = DefaultCategories.getCategories(isIncome = false)) }
    }

    override fun handleEvent(event: AddBudgetEvent) {
        when (event) {
            is AddBudgetEvent.OnCategorySelected -> updateCategory(event.category)
            is AddBudgetEvent.OnAmountChanged -> updateAmount(event.amount)
            is AddBudgetEvent.OnCurrencyChanged -> updateCurrency(event.currency)
            is AddBudgetEvent.OnPeriodChanged -> setState { copy(periodId = event.periodId) }
            is AddBudgetEvent.OnNotesChanged -> setState { copy(notes = event.notes) }
            AddBudgetEvent.OnSave -> saveBudget()
            AddBudgetEvent.OnCancel -> navigateBack()
        }
    }

    private fun updateCategory(category: Category) {
        setState { copy(selectedCategory = category).derived() }
    }

    private fun updateAmount(amount: String) {
        var dotSeen = false
        val sanitized = buildString {
            amount.forEach { char ->
                when {
                    char.isDigit() -> append(char)
                    char == '.' && !dotSeen -> {
                        append(char)
                        dotSeen = true
                    }
                }
            }
        }
        setState { copy(amountInput = sanitized).derived() }
    }

    private fun updateCurrency(currency: String) {
        setState { copy(currency = currency).derived() }
    }

    private fun saveBudget() {
        val currentState = _uiState.value
        if (!currentState.isSaveEnabled) return

        setState { copy(isSaving = true, error = null).derived() }

        viewModelScope.launch {
            try {
                val budget = Budget(
                    id = UUID.randomUUID().toString(),
                    userId = "A1", // TODO: Get actual user ID
                    category = currentState.selectedCategory?.id ?: "",
                    amount = currentState.amountInput.toDoubleOrNull() ?: 0.0,
                    currency = currentState.currency,
                    period = currentState.periodId,
                    notes = currentState.notes.ifBlank { null },
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )

                addBudgetUseCase(budget).onSuccess {
                    setState { copy(isSaving = false).derived() }
                    triggerSideEffect(AddBudgetSideEffect.ShowSuccess("Budget created successfully"))
                    triggerSideEffect(AddBudgetSideEffect.NavigateBack)
                }.onFailure { e ->
                    setState { copy(isSaving = false, error = "Failed to save budget").derived() }
                    triggerSideEffect(AddBudgetSideEffect.ShowError("Failed to save budget: ${e.message}"))
                }
            } catch (e: Exception) {
                setState { copy(isSaving = false, error = "Failed to save budget").derived() }
                triggerSideEffect(AddBudgetSideEffect.ShowError("Failed to save budget: ${e.message}"))
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            triggerSideEffect(AddBudgetSideEffect.NavigateBack)
        }
    }

    private fun AddBudgetState.derived(): AddBudgetState {
        val amount = amountInput.toDoubleOrNull() ?: 0.0
        val isEnabled = selectedCategory != null &&
                amount > 0.0 &&
                currency.isNotEmpty() &&
                !isSaving
        return copy(isSaveEnabled = isEnabled)
    }
}
