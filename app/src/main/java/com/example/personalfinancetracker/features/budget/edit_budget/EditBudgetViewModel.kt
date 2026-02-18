package com.example.personalfinancetracker.features.budget.edit_budget

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.core.common.BaseViewModel
import com.example.core.common.MVIUiEvent
import com.example.core.model.DefaultCategories
import com.example.core.model.DefaultCurrencies
import com.example.domain.ValidationResult
import com.example.domain.model.Budget
import com.example.domain.model.BudgetPeriod
import com.example.domain.usecase.ValidateInputsUseCase
import com.example.domain.usecase.budget_usecases.DeleteBudgetUseCase
import com.example.domain.usecase.budget_usecases.GetBudgetByIdUseCase
import com.example.domain.usecase.budget_usecases.GetBudgetTransactionsUseCase
import com.example.domain.usecase.budget_usecases.UpdateBudgetUseCase
import com.example.personalfinancetracker.features.budget.mapper.toBudget
import com.example.personalfinancetracker.features.budget.mapper.toBudgetUi
import com.example.personalfinancetracker.features.budget.model.BudgetInsightsUi
import com.example.personalfinancetracker.features.budget.model.BudgetUi
import com.example.personalfinancetracker.features.budget.utils.calculateDaysElapsed
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditBudgetViewModel(
    savedStateHandle: SavedStateHandle,
    private val getBudgetByIdUseCase: GetBudgetByIdUseCase,
    private val updateBudgetUseCase: UpdateBudgetUseCase,
    private val deleteBudgetUseCase: DeleteBudgetUseCase,
    private val getBudgetTransactionsUseCase: GetBudgetTransactionsUseCase,
    private val validatorUseCase: ValidateInputsUseCase
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
            getBudgetByIdUseCase(budgetId)
                .onSuccess { budget ->
                    if (budget == null) {
                        updateState { copy(isLoading = false, error = "Budget not found") }
                        return@launch
                    }
                    collectTransactions(budget)
                }
                .onFailure {
                    updateState { copy(isLoading = false, error = "Failed to load budget") }
                    showError("Failed to load budget")
                }
        }
    }

    private suspend fun collectTransactions(budget: Budget) {
        getBudgetTransactionsUseCase(budgetId)
            .catch { showError("Failed to load transactions") }
            .collect { transactions ->
                val spent = transactions.sumOf { it.amount }
                val budgetUi = budget.toBudgetUi(spent)
                val insights = calculateInsights(budgetUi)

                updateState {
                    copy(
                        budget = budgetUi,
                        insights = insights,
                        selectedCategory = budgetUi.currentCategory,
                        selectedCurrency = DefaultCurrencies.fromId(budgetUi.currency)
                            ?: DefaultCurrencies.USD,
                        categories = DefaultCategories.getCategories(isIncome = false),
                        amountInput = budgetUi.amount.toString(),
                        periodInput = BudgetPeriod.fromId(budgetUi.period),
                        notesInput = budgetUi.notes ?: "",
                        isLoading = false
                    )
                }
            }
    }

    private fun calculateInsights(budget: BudgetUi, days: Int? = null): BudgetInsightsUi {
        val daysInPeriod = days ?: BudgetPeriod.fromId( budget.period).days
        val daysElapsed = calculateDaysElapsed(budget.createdAt, daysInPeriod)
        val averageDailySpend = if (daysElapsed > 0) budget.spent / daysElapsed else 0.0
        val projectedTotal = if (daysElapsed > 0) averageDailySpend * daysInPeriod else budget.spent

        return BudgetInsightsUi(
            daysElapsed = daysElapsed,
            daysTotal = daysInPeriod,
            averageDailySpend = averageDailySpend,
            projectedTotal = projectedTotal,
            isProjectedOverBudget = budget.amount < projectedTotal
        )
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
                copy(amountInput = sanitizeAmount(event.amount))
            }

            is EditBudgetEvent.OnCurrencyChanged -> updateState { copy(selectedCurrency = event.currency) }
            is EditBudgetEvent.OnPeriodChanged -> updateState {

               val insights = calculateInsights(budget!!, event.period.days)
                copy(
                    periodInput = event.period,
                    insights = insights
                )
            }

            is EditBudgetEvent.OnNotesChanged -> updateState { copy(notesInput = event.notes) }
        }
    }

    private fun saveBudget() {
        val currentState = _uiState.value
        val amount = currentState.amountInput.toDoubleOrNull()

        val validationResult = validatorUseCase(
            category = currentState.selectedCategory?.id ?: "",
            amount = currentState.amountInput,
            currency = currentState.selectedCurrency?.id ?: "",
            date = currentState.budget?.updatedAt ?: 0L
        )

        if (validationResult is ValidationResult.Error) {
            showError(validationResult.message)
            return
        }

        updateState { copy(isLoading = true) }

        viewModelScope.launch {
            val budget = BudgetUi(
                id = budgetId,
                userId = currentState.budget?.userId ?: "",
                spent = currentState.budget?.spent ?: 0.0,
                category = currentState.selectedCategory?.id ?: "",
                amount = amount ?: 0.0,
                currency = currentState.selectedCurrency?.id ?: "",
                period = currentState.periodInput.id,
                notes = currentState.notesInput.ifBlank { null },
                createdAt = currentState.budget?.createdAt ?: 0L,
                updatedAt = System.currentTimeMillis()
            )

            updateBudgetUseCase(budget.toBudget())
                .onSuccess {
                    updateState { copy(isLoading = false, isEditing = false) }
                    triggerSideEffect(EditBudgetSideEffect.ShowSuccess("Budget updated"))
                }
                .onFailure {
                    updateState { copy(isLoading = false) }
                    showError("Failed to update: ${it.message}")
                }
        }
    }

    private fun deleteBudget() {
        updateState { copy(showDeleteConfirmation = false, isLoading = true) }

        viewModelScope.launch {
            deleteBudgetUseCase(budgetId)
                .onSuccess {
                    updateState { copy(isLoading = false) }
                    triggerSideEffect(EditBudgetSideEffect.ShowSuccess("Budget deleted"))
                    triggerSideEffect(EditBudgetSideEffect.NavigateBack)
                }
                .onFailure {
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
