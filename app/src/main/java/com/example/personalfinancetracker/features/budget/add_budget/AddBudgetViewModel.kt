package com.example.personalfinancetracker.features.budget.add_budget

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.ui.theme.ProgressError
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddBudgetViewModel : ViewModel() {

    private val _state = MutableStateFlow(
        AddBudgetContract.State()
    )
    val state: StateFlow<AddBudgetContract.State> = _state.asStateFlow()

    private val _sideEffects = MutableSharedFlow<AddBudgetContract.SideEffect>()
    val sideEffects: SharedFlow<AddBudgetContract.SideEffect> = _sideEffects.asSharedFlow()

    fun onEvent(event: AddBudgetContract.Event) {
        when (event) {
            AddBudgetContract.Event.OnLoad -> loadDefaults()
            is AddBudgetContract.Event.OnCategorySelected -> selectCategory(event.categoryId)
            is AddBudgetContract.Event.OnAmountChanged -> updateAmount(event.amount)
            is AddBudgetContract.Event.OnPeriodChanged -> updatePeriod(event.periodId)
            is AddBudgetContract.Event.OnNotesChanged -> updateNotes(event.notes)
            AddBudgetContract.Event.OnSave -> saveBudget()
            AddBudgetContract.Event.OnCancel -> navigateBack()
        }
    }

    private fun loadDefaults() {
        _state.value = _state.value.copy(
            categories = defaultCategories,
            periodId = AddBudgetContract.PeriodOptions.default.id
        )
    }

    private fun selectCategory(categoryId: String) {
        _state.value = _state.value.copy(categoryId = categoryId)
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
        _state.value = _state.value.copy(amountInput = sanitized)
    }

    private fun updatePeriod(periodId: String) {
        _state.value = _state.value.copy(periodId = periodId)
    }

    private fun updateNotes(notes: String) {
        _state.value = _state.value.copy(notes = notes)
    }

    private fun saveBudget() {
        val currentState = _state.value
        if (!currentState.isSaveEnabled) {
            return
        }

        viewModelScope.launch {
            _state.value = currentState.copy(isSaving = true, error = null)
            try {
                delay(800)
                // Placeholder for repository call
                _sideEffects.emit(AddBudgetContract.SideEffect.ShowSuccess("Budget created successfully"))
                _sideEffects.emit(AddBudgetContract.SideEffect.NavigateBack)
            } catch (throwable: Throwable) {
                _state.value = currentState.copy(isSaving = false, error = throwable.message ?: "Failed to save budget")
                _sideEffects.emit(AddBudgetContract.SideEffect.ShowError(_state.value.error!!))
            } finally {
                _state.value = _state.value.copy(isSaving = false)
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _sideEffects.emit(AddBudgetContract.SideEffect.NavigateBack)
        }
    }

    private val defaultCategories = listOf(
        AddBudgetContract.Category(
            id = "food",
            name = "Food & Dining",
            icon = Icons.Outlined.Home,
            iconTint = ProgressError,
            iconBackground = ProgressError.copy(alpha = 0.12f)
        ),
        AddBudgetContract.Category(
            id = "transport",
            name = "Transport",
            icon = Icons.Outlined.Home,
            iconTint = ColorPalette.Blue,
            iconBackground = ColorPalette.Blue.copy(alpha = 0.12f)
        ),
        AddBudgetContract.Category(
            id = "shopping",
            name = "Shopping",
            icon = Icons.Outlined.ShoppingCart,
            iconTint = ColorPalette.Purple,
            iconBackground = ColorPalette.Purple.copy(alpha = 0.12f)
        ),
        AddBudgetContract.Category(
            id = "bills",
            name = "Bills & Utilities",
            icon = Icons.Outlined.Home,
            iconTint = ColorPalette.Green,
            iconBackground = ColorPalette.Green.copy(alpha = 0.12f)
        ),
        AddBudgetContract.Category(
            id = "entertainment",
            name = "Entertainment",
            icon = Icons.Outlined.ShoppingCart,
            iconTint = ColorPalette.Pink,
            iconBackground = ColorPalette.Pink.copy(alpha = 0.12f)
        ),
        AddBudgetContract.Category(
            id = "health",
            name = "Health & Fitness",
            icon = Icons.Outlined.FavoriteBorder,
            iconTint = ColorPalette.Red,
            iconBackground = ColorPalette.Red.copy(alpha = 0.12f)
        ),
        AddBudgetContract.Category(
            id = "education",
            name = "Education",
            icon = Icons.Outlined.Home,
            iconTint = ColorPalette.Indigo,
            iconBackground = ColorPalette.Indigo.copy(alpha = 0.12f)
        ),
        AddBudgetContract.Category(
            id = "travel",
            name = "Travel",
            icon = Icons.Outlined.Face,
            iconTint = ColorPalette.Cyan,
            iconBackground = ColorPalette.Cyan.copy(alpha = 0.12f)
        )
    )

    private object ColorPalette {
        val Blue = Color(0xFF3B82F6)
        val Purple = Color(0xFF8B5CF6)
        val Green = Color(0xFF22C55E)
        val Pink = Color(0xFFEC4899)
        val Red = Color(0xFFEF4444)
        val Indigo = Color(0xFF6366F1)
        val Cyan = Color(0xFF06B6D4)
    }
}
