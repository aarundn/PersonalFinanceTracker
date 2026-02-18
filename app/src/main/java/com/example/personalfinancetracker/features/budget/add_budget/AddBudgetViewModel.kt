package com.example.personalfinancetracker.features.budget.add_budget

import androidx.lifecycle.viewModelScope
import com.example.core.common.BaseViewModel
import com.example.core.model.Category
import com.example.core.model.Currency
import com.example.core.model.DefaultCategories
import com.example.domain.ValidationResult
import com.example.domain.model.Budget
import com.example.domain.usecase.ValidateInputsUseCase
import com.example.domain.usecase.budget_usecases.AddBudgetUseCase
import kotlinx.coroutines.launch
import java.util.UUID

class AddBudgetViewModel(
    private val addBudgetUseCase: AddBudgetUseCase,
    private val validatorUseCase: ValidateInputsUseCase
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
            is AddBudgetEvent.OnPeriodChanged -> setState { copy(period = event.period) }
            is AddBudgetEvent.OnNotesChanged -> setState { copy(notes = event.notes) }
            AddBudgetEvent.OnSave -> saveBudget()
            AddBudgetEvent.OnCancel -> navigateBack()
        }
    }

    private fun updateCategory(category: Category) {
        setState { copy(selectedCategory = category) }
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
        setState { copy(amountInput = sanitized) }
    }

    private fun updateCurrency(currency: Currency) {
        setState { copy(selectedCurrency = currency) }
    }

    private fun saveBudget() {
        val currentState = _uiState.value

        val validationResult = validatorUseCase(
            category = currentState.selectedCategory?.id ?: "",
            amount = currentState.amountInput,
            currency = currentState.selectedCurrency?.id ?: "",
            date = System.currentTimeMillis()
        )

        if (validationResult is ValidationResult.Error) {
            triggerSideEffect(AddBudgetSideEffect.ShowError(validationResult.message))
            return
        }
        setState { copy(isSaving = true, error = null) }

        viewModelScope.launch {
            try {
                val budget = Budget(
                    id = UUID.randomUUID().toString(),
                    userId = "A1", // TODO: Get actual user ID
                    category = currentState.selectedCategory?.id ?: "",
                    amount = currentState.amountInput.toDoubleOrNull() ?: 0.0,
                    currency = currentState.selectedCurrency?.id ?: "",
                    period = currentState.period.id,
                    notes = currentState.notes.ifBlank { null },
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )

                addBudgetUseCase(budget).onSuccess {
                    setState { copy(isSaving = false) }
                    triggerSideEffect(AddBudgetSideEffect.ShowSuccess("Budget created successfully"))
                    triggerSideEffect(AddBudgetSideEffect.NavigateBack)
                }.onFailure { e ->
                    setState { copy(isSaving = false, error = "Failed to save budget") }
                    triggerSideEffect(AddBudgetSideEffect.ShowError("Failed to save budget: ${e.message}"))
                }
            } catch (e: Exception) {
                setState { copy(isSaving = false, error = "Failed to save budget") }
                triggerSideEffect(AddBudgetSideEffect.ShowError("Failed to save budget: ${e.message}"))
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            triggerSideEffect(AddBudgetSideEffect.NavigateBack)
        }
    }
}
