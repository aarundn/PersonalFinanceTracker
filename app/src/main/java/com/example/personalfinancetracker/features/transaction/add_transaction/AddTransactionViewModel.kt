package com.example.personalfinancetracker.features.transaction.add_transaction

import androidx.lifecycle.viewModelScope
import com.example.core.common.BaseViewModel
import com.example.core.common.MVIUiEvent
import com.example.core.model.DefaultCategories
import com.example.domain.ValidationResult
import com.example.domain.model.Type
import com.example.domain.usecase.budget_usecases.GetBudgetTransactionsUseCase
import com.example.domain.usecase.budget_usecases.GetBudgetsByCategoryUseCase
import com.example.domain.usecase.transaction_usecases.AddTransactionUseCase
import com.example.domain.usecase.ValidateInputsUseCase
import com.example.personalfinancetracker.features.budget.mapper.toBudgetUi
import com.example.personalfinancetracker.features.transaction.mapper.toTransaction
import com.example.personalfinancetracker.features.transaction.model.TransactionUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID
import com.example.core.common.UiText

class AddTransactionViewModel(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val validateInputsUseCase: ValidateInputsUseCase,
    private val getBudgetsByCategoryUseCase: GetBudgetsByCategoryUseCase,
    private val getBudgetTransactionsUseCase: GetBudgetTransactionsUseCase
) : BaseViewModel<AddTransactionState, MVIUiEvent, AddTransactionSideEffect>() {

    private var budgetCollectionJob: Job? = null

    init {
        refreshCategories()
    }

    private fun refreshCategories() {
        val categories = DefaultCategories.getCategories(_uiState.value.isIncome)
        setState { copy(categories = categories) }
    }

    override fun createInitialState() = AddTransactionState()

    override fun handleEvent(event: MVIUiEvent) {

        when (event) {
            is AddTransactionEvent.OnTransactionTypeChanged -> {
                setState {
                    copy(
                        isIncome = event.isIncome,
                        selectedCategory = null,
                        selectedBudget = null,
                        availableBudgets = emptyList()
                    )
                }
                refreshCategories()
                cancelBudgetCollection()
            }

            is AddTransactionEvent.OnCategoryChanged -> {
                setState {
                    copy(
                        selectedCategory = event.category,
                        selectedBudget = null,
                        availableBudgets = emptyList()
                    )
                }
                if (!_uiState.value.isIncome) {
                    loadBudgetsForCategory(event.category.id)
                }
            }

            is AddTransactionEvent.OnAmountChanged -> setState { copy(amount = event.amount) }
            is AddTransactionEvent.OnCurrencyChanged -> setState { copy(selectedCurrency = event.currency) }
            is AddTransactionEvent.OnDateChanged -> setState {
                copy(
                    date = event.date,
                    showDatePicker = false
                )
            }

            is AddTransactionEvent.OnNotesChanged -> setState { copy(notes = event.notes) }
            is AddTransactionEvent.OnBudgetSelected -> setState {
                copy(
                    selectedBudget = _uiState.value.availableBudgets.find { it.id == event.budgetId },
                    showBudgetSelector = false
                )
            }

            AddTransactionEvent.OnAddBudgetClicked -> navigateToAddBudget()
            AddTransactionEvent.OnShowBudgetSelector -> setState { copy(showBudgetSelector = true) }
            AddTransactionEvent.OnHideBudgetSelector -> setState { copy(showBudgetSelector = false) }
            AddTransactionEvent.OnShowDatePicker -> setState { copy(showDatePicker = true) }
            AddTransactionEvent.OnHideDatePicker -> setState { copy(showDatePicker = false) }
            AddTransactionEvent.OnSave -> saveTransaction()
            AddTransactionEvent.OnCancel -> navigateBack()
        }
    }

    private fun cancelBudgetCollection() {
        budgetCollectionJob?.cancel()
        budgetCollectionJob = null
    }

    private fun loadBudgetsForCategory(categoryId: String) {
        cancelBudgetCollection()
        budgetCollectionJob = viewModelScope.launch {
            getBudgetsByCategoryUseCase(categoryId).collectLatest { budgets ->
                if (budgets.isEmpty()) {
                    setState { copy(availableBudgets = emptyList()) }
                } else {
                    val budgetFlows = budgets.map { budget ->
                        getBudgetTransactionsUseCase(budget.id).map { transactions ->
                            val spent = transactions.sumOf { it.amount }
                            budget.toBudgetUi(spent)
                        }
                    }
                    combine(budgetFlows) { budgetUis ->
                        budgetUis.toList()
                    }.collectLatest { budgetUiList ->
                        setState { copy(availableBudgets = budgetUiList) }
                    }
                }
            }
        }
    }


    private fun navigateBack() {
        viewModelScope.launch {
            triggerSideEffect(AddTransactionSideEffect.NavigateBack)
        }
    }

    private fun navigateToAddBudget() {
        viewModelScope.launch {
            triggerSideEffect(AddTransactionSideEffect.NavigateToAddBudget)
        }
    }

    private fun saveTransaction() {
        val currentState = _uiState.value

        val validationResult = validateInputsUseCase(
            category = currentState.selectedCategory?.id ?: "",
            amount = currentState.amount,
            currency = currentState.selectedCurrency?.id ?: "",
            date = currentState.date
        )

        if (validationResult is ValidationResult.Error) {
            showError(UiText.DynamicString(validationResult.message))
            return
        }


        setState { copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val transactionData = TransactionUi(
                    id = UUID.randomUUID().toString(),
                    userId = "A1", // TODO: Get actual user ID
                    amount = currentState.amount.toDoubleOrNull() ?: 0.0,
                    currency = currentState.selectedCurrency?.id ?: "",
                    categoryId = currentState.selectedCategory?.id ?: "",
                    budgetId = currentState.selectedBudget?.id,
                    date = currentState.date,
                    notes = currentState.notes,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    type = if (currentState.isIncome) Type.INCOME else Type.EXPENSE,
                ).toTransaction()

                addTransactionUseCase(transactionData).onSuccess {
                    setState { copy(isLoading = false) }
                    triggerSideEffect(AddTransactionSideEffect.NavigateBack)
                    triggerSideEffect(AddTransactionSideEffect.ShowSuccess(UiText.DynamicString("Transaction saved successfully")))
                }.onFailure { e ->
                    setState {
                        copy(
                            isLoading = false,
                            error = UiText.DynamicString("Failed to save transaction")
                        )
                    }
                    triggerSideEffect(AddTransactionSideEffect.ShowError(UiText.DynamicString("Failed to save transaction: ${e.message}")))
                }

            } catch (e: Exception) {
                setState {
                    copy(
                        isLoading = false,
                        error = UiText.DynamicString("Failed to save transaction")
                    )
                }
                triggerSideEffect(AddTransactionSideEffect.ShowError(UiText.DynamicString("Failed to save transaction: ${e.message}")))
            }
        }
    }

    private fun showError(message: UiText) {
        viewModelScope.launch {
            triggerSideEffect(AddTransactionSideEffect.ShowError(message))
        }
    }

}