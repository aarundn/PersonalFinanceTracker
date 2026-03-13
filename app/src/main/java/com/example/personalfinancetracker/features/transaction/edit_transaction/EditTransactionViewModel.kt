package com.example.personalfinancetracker.features.transaction.edit_transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.core.common.BaseViewModel
import com.example.core.common.MVIUiEvent
import com.example.core.model.DefaultCategories
import com.example.core.model.DefaultCurrencies
import com.example.domain.ValidationResult
import com.example.domain.model.Type
import com.example.domain.usecase.ValidateInputsUseCase
import com.example.domain.usecase.transaction_usecases.DeleteTransactionUseCase
import com.example.domain.usecase.transaction_usecases.GetTransactionByIdUseCase
import com.example.domain.usecase.transaction_usecases.UpdateTransactionUseCase
import com.example.domain.usecase.budget_usecases.GetBudgetTransactionsUseCase
import com.example.domain.usecase.budget_usecases.GetBudgetsByCategoryUseCase
import com.example.personalfinancetracker.features.budget.mapper.toBudgetUi
import com.example.personalfinancetracker.features.transaction.mapper.toTransaction
import com.example.personalfinancetracker.features.transaction.mapper.toTransactionUi
import com.example.personalfinancetracker.features.transaction.model.TransactionUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.core.R
import com.example.core.common.UiText

class EditTransactionViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val validateInputsUseCase: ValidateInputsUseCase,
    private val getBudgetsByCategoryUseCase: GetBudgetsByCategoryUseCase,
    private val getBudgetTransactionsUseCase: GetBudgetTransactionsUseCase
) : BaseViewModel<EditTransactionState, MVIUiEvent, EditTransactionSideEffect>() {

    companion object {
        const val TRANSACTION_ID_KEY = "transactionId"
    }

    private val transactionId: String = savedStateHandle.get<String>(TRANSACTION_ID_KEY) ?: ""
    private var budgetCollectionJob: Job? = null

    override fun createInitialState() = EditTransactionState()


    init {
        loadTransaction()
    }

    private fun loadTransaction() {
        if (transactionId.isEmpty()) {
            updateState { copy(isLoading = false, error = UiText.StringResource(R.string.error_invalid_transaction_id)) }
            return
        }
        updateState { copy(isLoading = true) }

        viewModelScope.launch {

            getTransactionByIdUseCase(transactionId).onSuccess { transaction ->
                if (transaction == null)
                    updateState { copy(isLoading = false, error = UiText.StringResource(R.string.error_transaction_not_found)) }

                val txn = transaction?.toTransactionUi() ?: return@onSuccess

                val categories = DefaultCategories.getCategories(txn.isIncome)
                val selectedCategory = DefaultCategories.fromId(txn.categoryId)

                updateState {
                    copy(
                        transaction = txn,
                        isLoading = false,
                        isIncome = txn.isIncome,
                        selectedCategory = selectedCategory,
                        amount = txn.amount.toString(),
                        selectedCurrency = DefaultCurrencies.fromId(txn.currency),
                        date = txn.date,
                        notes = txn.notes ?: "",
                        categories = categories
                    )
                }

                if (!txn.isIncome && selectedCategory != null) {
                    loadBudgetsForCategory(selectedCategory.id, txn.budgetId)
                }
            }.onFailure { e ->
                updateState { copy(isLoading = false, error = UiText.StringResource(R.string.error_failed_load_transaction)) }
            }

        }
    }

    override fun handleEvent(event: MVIUiEvent) {
        when (event) {
            is EditTransactionEvent.OnTransactionTypeChanged -> {
                updateState { copy(isIncome = event.isIncome, selectedCategory = null, selectedBudget = null, availableBudgets = emptyList()) }
                refreshCategories()
                cancelBudgetCollection()
            }

            is EditTransactionEvent.OnCategoryChanged -> {
                updateState { copy(selectedCategory = event.category, selectedBudget = null, availableBudgets = emptyList()) }
                if (!_uiState.value.isIncome) {
                    loadBudgetsForCategory(event.category.id)
                }
            }
            is EditTransactionEvent.OnAmountChanged -> updateState { copy(amount = event.amount) }
            is EditTransactionEvent.OnCurrencyChanged -> updateState { copy(selectedCurrency = event.currency) }
            is EditTransactionEvent.OnDateChanged -> updateState { copy(date = event.date, showDatePicker = false) }
            is EditTransactionEvent.OnNotesChanged -> updateState { copy(notes = event.notes) }
            EditTransactionEvent.OnSave -> saveTransaction()
            EditTransactionEvent.OnCancel -> navigateBack()

            EditTransactionEvent.OnDelete -> updateState { copy(showDeleteConfirmation = true) }
            EditTransactionEvent.OnDismissDelete -> updateState { copy(showDeleteConfirmation = false) }
            EditTransactionEvent.OnConfirmDelete -> deleteTransaction()
            EditTransactionEvent.OnEdit -> updateState { copy(isEditing = true) }
            EditTransactionEvent.OnShowDatePicker -> updateState { copy(showDatePicker = true) }
            EditTransactionEvent.OnHideDatePicker -> updateState { copy(showDatePicker = false) }

            is EditTransactionEvent.OnBudgetSelected -> updateState {
                copy(
                    selectedBudget = _uiState.value.availableBudgets.find { it.id == event.budgetId },
                    showBudgetSelector = false
                )
            }
            EditTransactionEvent.OnShowBudgetSelector -> updateState { copy(showBudgetSelector = true) }
            EditTransactionEvent.OnHideBudgetSelector -> updateState { copy(showBudgetSelector = false) }
            EditTransactionEvent.OnAddBudgetClicked -> triggerNavigateToAddBudget()
        }
    }

    private fun triggerNavigateToAddBudget() {
        viewModelScope.launch {
            triggerSideEffect(EditTransactionSideEffect.NavigateToAddBudget)
        }
    }

    private fun cancelBudgetCollection() {
        budgetCollectionJob?.cancel()
        budgetCollectionJob = null
    }

    private fun loadBudgetsForCategory(categoryId: String, initialBudgetId: String? = null) {
        cancelBudgetCollection()
        budgetCollectionJob = viewModelScope.launch {
            getBudgetsByCategoryUseCase(categoryId).collectLatest { budgets ->
                if (budgets.isEmpty()) {
                    updateState { copy(availableBudgets = emptyList(), selectedBudget = null) }
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
                        updateState {
                            copy(
                                availableBudgets = budgetUiList,
                                selectedBudget = if (initialBudgetId != null) budgetUiList.find { it.id == initialBudgetId } else selectedBudget
                            )
                        }
                    }
                }
            }
        }
    }


    private fun updateState(update: EditTransactionState.() -> EditTransactionState) {
        _uiState.update { it.update() }
    }

    private fun refreshCategories() {
        val categories = DefaultCategories.getCategories(_uiState.value.isIncome)
        updateState { copy(categories = categories) }
    }


    private fun saveTransaction() {
        val currentState = uiState.value

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

        updateState { copy(isLoading = true) }

        viewModelScope.launch {
            try {

                val transactionData = TransactionUi(
                    id = transactionId,
                    userId = "A1", // TODO: Get actual user ID
                    amount = currentState.amount.toDoubleOrNull() ?: 0.0,
                    currency = currentState.selectedCurrency?.id ?: "",
                    categoryId = currentState.selectedCategory?.id ?: "",
                    budgetId = currentState.selectedBudget?.id,
                    date = currentState.date,
                    notes = currentState.notes,
                    createdAt = currentState.transaction?.createdAt ?: currentState.date,
                    updatedAt = System.currentTimeMillis(),
                    type = if (currentState.isIncome) Type.INCOME else Type.EXPENSE,
                ).toTransaction()

                updateTransactionUseCase(transactionData).onSuccess {
                    updateState { copy(isLoading = false, isEditing = false) }
                    triggerSideEffect(EditTransactionSideEffect.ShowSuccess(UiText.StringResource(R.string.success_transaction_updated)))
                    triggerSideEffect(EditTransactionSideEffect.NavigateBack)
                }.onFailure { e ->
                    updateState { copy(isLoading = false, error = UiText.StringResource(R.string.error_failed_update_transaction)) }
                    triggerSideEffect(EditTransactionSideEffect.ShowError(UiText.DynamicString("Failed to update transaction: ${e.message}")))
                }

            } catch (e: Exception) {
                updateState { copy(isLoading = false, error = UiText.StringResource(R.string.error_failed_update_transaction)) }
                showError(UiText.DynamicString(e.message ?: "Failed to update transaction"))
            }
        }
    }

    private fun deleteTransaction() {
        updateState { copy(showDeleteConfirmation = false, isLoading = true) }

        viewModelScope.launch {
            deleteTransactionUseCase(transactionId).onSuccess {
                updateState { copy(isLoading = false) }
                triggerSideEffect(EditTransactionSideEffect.ShowSuccess(UiText.StringResource(R.string.success_transaction_deleted)))
                triggerSideEffect(EditTransactionSideEffect.NavigateBack)
            }.onFailure { e ->
                updateState { copy(isLoading = false, error = UiText.StringResource(R.string.error_failed_delete_transaction)) }
                triggerSideEffect(EditTransactionSideEffect.ShowError(UiText.DynamicString("Failed to delete transaction: ${e.message}")))
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            triggerSideEffect(EditTransactionSideEffect.NavigateBack)
        }
    }

    private fun showError(message: UiText) {
        viewModelScope.launch {
            triggerSideEffect(EditTransactionSideEffect.ShowError(message))
        }
    }
}
