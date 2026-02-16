package com.example.personalfinancetracker.features.budget.edit_budget

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.core.common.BaseViewModel
import com.example.core.common.MVIUiEvent
import com.example.core.model.DefaultCategories
import com.example.core.model.DefaultCurrencies
import com.example.domain.model.Budget
import com.example.domain.usecase.budget_usecases.DeleteBudgetUseCase
import com.example.domain.usecase.budget_usecases.GetBudgetByIdUseCase
import com.example.domain.usecase.budget_usecases.UpdateBudgetUseCase
import com.example.domain.usecase.transaction_usecases.GetTransactionsUseCase
import com.example.personalfinancetracker.features.budget.mapper.toBudget
import com.example.personalfinancetracker.features.budget.mapper.toBudgetUi
import com.example.personalfinancetracker.features.budget.model.BudgetUi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditBudgetViewModel(
    savedStateHandle: SavedStateHandle,
    private val getBudgetByIdUseCase: GetBudgetByIdUseCase,
    private val updateBudgetUseCase: UpdateBudgetUseCase,
    private val deleteBudgetUseCase: DeleteBudgetUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
) : BaseViewModel<EditBudgetState, MVIUiEvent, EditBudgetSideEffect>() {

    companion object {
        const val BUDGET_ID_KEY = "budgetId"
    }

    private val budgetId: String = savedStateHandle.get<String>(BUDGET_ID_KEY) ?: ""

    override fun createInitialState() = EditBudgetState()

    init {
        loadData()
    }

    private fun updateState(update: EditBudgetState.() -> EditBudgetState) {
        _uiState.update { it.update() }
    }

    private fun showError(message: String) {
        viewModelScope.launch {
            triggerSideEffect(EditBudgetSideEffect.ShowError(message))
        }
    }

    private fun loadData() {
        if (budgetId.isBlank()) {
            updateState { copy(isLoading = false, error = "Invalid budget ID") }
            return
        }

        updateState { copy(isLoading = true) }

        viewModelScope.launch {
            try {
                getBudgetByIdUseCase(budgetId).onSuccess { budget ->
                    budget?.let {

                        collectTransactions(budget)

                    } ?: run {
                        updateState { copy(isLoading = false, error = "Budget not found") }
                    }
                }.onFailure {
                    updateState { copy(isLoading = false, error = "Failed to load budget") }
                    showError("Failed to load budget")
                }
            } catch (e: Exception) {
                updateState { copy(isLoading = false, error = e.message) }
                showError(e.message ?: "Failed to load budget")
            }
        }
    }

    private suspend fun collectTransactions(
        budget: Budget
    ) {
        getTransactionsUseCase()
            .catch { showError("Failed to load transactions") }
            .collect { transactions ->
                val budgetUi = budget.toBudgetUi(transactions)
                updateState {
                    copy(
                        budget = budgetUi,
                        selectedCategory = budgetUi.currentCategory,
                        selectedCurrency = DefaultCurrencies.fromId(budgetUi.currency)
                            ?: DefaultCurrencies.USD,
                        categories = DefaultCategories.getCategories(isIncome = false),
                        amountInput = budgetUi.amount.toString(),
                        periodInput = budgetUi.period,
                        notesInput = budgetUi.notes ?: "",
                        isLoading = false
                    )
                }
            }
    }

    override fun handleEvent(event: MVIUiEvent) {
        when (event) {
            EditBudgetEvent.OnEdit -> updateState { copy(isEditing = true) }
            EditBudgetEvent.OnCancel -> navigateBack()
            EditBudgetEvent.OnSave -> saveBudget()
            EditBudgetEvent.OnDelete -> updateState { copy(showDeleteConfirmation = true) }
            EditBudgetEvent.OnConfirmDelete -> deleteBudget()
            EditBudgetEvent.OnDismissDelete -> updateState { copy(showDeleteConfirmation = false) }
            is EditBudgetEvent.OnCategoryChanged -> updateState { copy(selectedCategory = event.category) }
            is EditBudgetEvent.OnAmountChanged -> updateState {
                copy(
                    amountInput = sanitizeAmount(
                        event.amount
                    )
                )
            }

            is EditBudgetEvent.OnCurrencyChanged -> updateState { copy(selectedCurrency = event.currency) }
            is EditBudgetEvent.OnPeriodChanged -> updateState { copy(periodInput = event.periodId) }
            is EditBudgetEvent.OnNotesChanged -> updateState { copy(notesInput = event.notes) }
        }
    }

    private fun saveBudget() {
        val currentState = _uiState.value
        val amount = currentState.amountInput.toDoubleOrNull()

        if (currentState.selectedCategory == null) {
            showError("Please select a category")
            return
        }

        if (amount == null || amount <= 0.0) {
            showError("Please enter a valid amount")
            return
        }

        updateState { copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val budget = BudgetUi(
                    id = budgetId,
                    userId = currentState.budget?.userId ?: "",
                    spent = currentState.budget?.spent ?: 0.0,
                    category = currentState.selectedCategory.id,
                    amount = amount,
                    currency = currentState.selectedCurrency?.id ?: "",
                    period = currentState.periodInput,
                    notes = currentState.notesInput.ifBlank { null },
                    createdAt = currentState.budget?.createdAt ?: 0L,
                    updatedAt = System.currentTimeMillis()
                )

                updateBudgetUseCase(budget.toBudget()).onSuccess {
                    updateState { copy(isLoading = false, isEditing = false) }
                    triggerSideEffect(EditBudgetSideEffect.ShowSuccess("Budget updated"))
                }.onFailure {
                    updateState { copy(isLoading = false) }
                    showError("Failed to update: ${it.message}")
                }
            } catch (e: Exception) {
                updateState { copy(isLoading = false) }
                showError("Error: ${e.message}")
            }
        }
    }

    private fun deleteBudget() {
        updateState { copy(showDeleteConfirmation = false, isLoading = true) }

        viewModelScope.launch {
            deleteBudgetUseCase(budgetId).onSuccess {
                updateState { copy(isLoading = false) }
                triggerSideEffect(EditBudgetSideEffect.ShowSuccess("Budget deleted"))
                triggerSideEffect(EditBudgetSideEffect.NavigateBack)
            }.onFailure {
                updateState { copy(isLoading = false) }
                showError("Failed to delete: ${it.message}")
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            triggerSideEffect(EditBudgetSideEffect.NavigateBack)
        }
    }

    private fun sanitizeAmount(amount: String): String {
        return amount.filterIndexed { index, char ->
            char.isDigit() || (char == '.' && amount.indexOf('.') == index)
        }
    }
}
