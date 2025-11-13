package com.example.personalfinancetracker.features.budget.edit_budget

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.max

class EditBudgetViewModel : ViewModel() {

    private val _state = MutableStateFlow(EditBudgetContract.State())
    val state: StateFlow<EditBudgetContract.State> = _state.asStateFlow()

    private val _sideEffects = MutableSharedFlow<EditBudgetContract.SideEffect>()
    val sideEffects: SharedFlow<EditBudgetContract.SideEffect> = _sideEffects.asSharedFlow()

    fun onEvent(event: EditBudgetContract.Event) {
        when (event) {
            EditBudgetContract.Event.OnLoadBudget -> loadBudget()
            EditBudgetContract.Event.OnEdit -> startEditing()
            EditBudgetContract.Event.OnCancel -> navigateBack()
            EditBudgetContract.Event.OnCancelEdit -> cancelEditing()
            EditBudgetContract.Event.OnSave -> saveBudget()
            EditBudgetContract.Event.OnDelete -> showDeleteDialog()
            EditBudgetContract.Event.OnConfirmDelete -> confirmDelete()
            EditBudgetContract.Event.OnDismissDeleteDialog -> dismissDeleteDialog()
            is EditBudgetContract.Event.OnBudgetAmountChanged -> updateBudgetAmount(event.amount)
            is EditBudgetContract.Event.OnPeriodChanged -> updatePeriod(event.periodId)
            is EditBudgetContract.Event.OnNotesChanged -> updateNotes(event.notes)
        }
    }

    fun setBudgetId(budgetId: Int) {
        _state.value = _state.value.copy(budgetId = budgetId)
    }

    private fun loadBudget() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            try {
                delay(500)
                val budget = getMockBudgetById(_state.value.budgetId)
                val periodOption = EditBudgetContract.PeriodOptions.findById(budget.period)
                _state.value = _state.value.copy(
                    category = budget.category,
                    budgetedAmountOriginal = budget.budgeted,
                    budgetedAmountInput = budget.budgeted.formatAmount(),
                    spentAmount = budget.spent,
                    periodOriginal = budget.period,
                    periodInput = budget.period,
                    notesOriginal = budget.notes ?: "",
                    notesInput = budget.notes ?: "",
                    icon = budget.icon,
                    iconTint = budget.iconTint,
                    iconBackground = budget.iconBackground,
                    daysInPeriod = periodOption.days,
                    daysElapsed = budget.daysElapsed,
                    isLoading = false,
                    error = null
                )
            } catch (throwable: Throwable) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = throwable.message ?: "Failed to load budget"
                )
                _sideEffects.emit(EditBudgetContract.SideEffect.ShowError(_state.value.error!!))
            }
        }
    }

    private fun startEditing() {
        _state.value = _state.value.copy(isEditing = true)
    }

    private fun cancelEditing() {
        _state.value = _state.value.copy(
            isEditing = false,
            budgetedAmountInput = _state.value.budgetedAmountOriginal.formatAmount(),
            periodInput = _state.value.periodOriginal,
            notesInput = _state.value.notesOriginal,
            daysInPeriod = EditBudgetContract.PeriodOptions.findById(_state.value.periodOriginal).days
        )
    }

    private fun saveBudget() {
        val currentState = _state.value
        val newBudgetValue = currentState.budgetedAmountInput.toDoubleOrNull()

        if (newBudgetValue == null || newBudgetValue <= 0.0) {
            viewModelScope.launch {
                _sideEffects.emit(
                    EditBudgetContract.SideEffect.ShowError("Please enter a valid budget amount")
                )
            }
            return
        }

        viewModelScope.launch {
            _state.value = currentState.copy(isLoading = true)

            try {
                delay(800)
                val selectedPeriod = EditBudgetContract.PeriodOptions.findById(currentState.periodInput)
                _state.value = currentState.copy(
                    budgetedAmountOriginal = newBudgetValue,
                    budgetedAmountInput = newBudgetValue.formatAmount(),
                    periodOriginal = selectedPeriod.id,
                    periodInput = selectedPeriod.id,
                    notesOriginal = currentState.notesInput,
                    isLoading = false,
                    isEditing = false,
                    daysInPeriod = selectedPeriod.days
                )
                _sideEffects.emit(
                    EditBudgetContract.SideEffect.ShowSuccess("Budget updated successfully")
                )
            } catch (throwable: Throwable) {
                _state.value = currentState.copy(isLoading = false)
                _sideEffects.emit(
                    EditBudgetContract.SideEffect.ShowError(
                        throwable.message ?: "Failed to update budget"
                    )
                )
            }
        }
    }

    private fun showDeleteDialog() {
        _state.value = _state.value.copy(showDeleteDialog = true)
    }

    private fun dismissDeleteDialog() {
        _state.value = _state.value.copy(showDeleteDialog = false)
    }

    private fun confirmDelete() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, showDeleteDialog = false)
            delay(600)
            _state.value = _state.value.copy(isLoading = false)
            _sideEffects.emit(EditBudgetContract.SideEffect.ShowSuccess("Budget deleted"))
            _sideEffects.emit(EditBudgetContract.SideEffect.NavigateBack)
        }
    }

    private fun updateBudgetAmount(amount: String) {
        _state.value = _state.value.copy(budgetedAmountInput = amount)
    }

    private fun updatePeriod(periodId: String) {
        val option = EditBudgetContract.PeriodOptions.findById(periodId)
        val adjustedDaysElapsed = max(0, minOf(_state.value.daysElapsed, option.days))
        _state.value = _state.value.copy(
            periodInput = option.id,
            daysInPeriod = option.days,
            daysElapsed = adjustedDaysElapsed
        )
    }

    private fun updateNotes(notes: String) {
        _state.value = _state.value.copy(notesInput = notes)
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _sideEffects.emit(EditBudgetContract.SideEffect.NavigateBack)
        }
    }

    private fun Double.formatAmount(): String = "%,.2f".format(this)

    private fun getMockBudgetById(budgetId: Int): MockBudget {
        return when (budgetId) {
            1 -> MockBudget(
                id = 1,
                category = "Food & Dining",
                budgeted = 500.0,
                spent = 385.0,
                period = EditBudgetContract.PeriodOptions.Monthly.id,
                notes = "Includes groceries and dining out",
                icon = Icons.Outlined.Home,
                iconTint = Color(0xFFF97316),
                iconBackground = Color(0xFFFFEDD5),
                daysElapsed = 12
            )
            2 -> MockBudget(
                id = 2,
                category = "Transport",
                budgeted = 300.0,
                spent = 265.0,
                period = EditBudgetContract.PeriodOptions.Monthly.id,
                notes = "Gas, parking, and public transport",
                icon = Icons.Outlined.Home,
                iconTint = Color(0xFF3B82F6),
                iconBackground = Color(0xFFEFF6FF),
                daysElapsed = 16
            )
            3 -> MockBudget(
                id = 3,
                category = "Shopping",
                budgeted = 400.0,
                spent = 520.0,
                period = EditBudgetContract.PeriodOptions.Monthly.id,
                notes = "Clothing and personal items",
                icon = Icons.Outlined.Home,
                iconTint = Color(0xFF8B5CF6),
                iconBackground = Color(0xFFF3E8FF),
                daysElapsed = 20
            )
            4 -> MockBudget(
                id = 4,
                category = "Bills & Utilities",
                budgeted = 600.0,
                spent = 420.0,
                period = EditBudgetContract.PeriodOptions.Monthly.id,
                notes = "Utilities, internet, and subscriptions",
                icon = Icons.Outlined.Home,
                iconTint = Color(0xFF22C55E),
                iconBackground = Color(0xFFF0FDF4),
                daysElapsed = 10
            )
            else -> MockBudget(
                id = budgetId,
                category = "Travel",
                budgeted = 800.0,
                spent = 450.0,
                period = EditBudgetContract.PeriodOptions.Quarterly.id,
                notes = "Weekend getaways and flights",
                icon = Icons.Outlined.Home,
                iconTint = Color(0xFF06B6D4),
                iconBackground = Color(0xFFECFEFF),
                daysElapsed = 25
            )
        }
    }

    private data class MockBudget(
        val id: Int,
        val category: String,
        val budgeted: Double,
        val spent: Double,
        val period: String,
        val notes: String?,
        val icon: ImageVector,
        val iconTint: Color,
        val iconBackground: Color,
        val daysElapsed: Int
    )
}
