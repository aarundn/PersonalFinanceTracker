package com.example.personalfinancetracker.features.budget.add_budget

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

object AddBudgetContract {

    @Immutable
    data class State(
        val categoryId: String = "",
        val amountInput: String = "",
        val periodId: String = PeriodOptions.default.id,
        val notes: String = "",
        val isSaving: Boolean = false,
        val isLoading: Boolean = false,
        val error: String? = null,
        val categories: List<Category> = emptyList()
    ) {
        val amount: Double get() = amountInput.toDoubleOrNull() ?: 0.0
        val isSaveEnabled: Boolean get() = categoryId.isNotBlank() && amount > 0.0 && !isSaving
        val selectedCategory: Category? get() = categories.find { it.id == categoryId }
        val period: PeriodOption get() = PeriodOptions.findById(periodId)
        val dailyAverage: Double
            get() = if (period.days > 0 && amount > 0) amount / period.days else 0.0
    }

    @Immutable
    data class Category(
        val id: String,
        val name: String,
        val icon: ImageVector,
        val iconTint: Color,
        val iconBackground: Color
    )

    @Immutable
    data class PeriodOption(
        val id: String,
        val label: String,
        val days: Int
    )

    object PeriodOptions {
        val Weekly = PeriodOption(id = "weekly", label = "Weekly", days = 7)
        val Monthly = PeriodOption(id = "monthly", label = "Monthly", days = 30)
        val Quarterly = PeriodOption(id = "quarterly", label = "Quarterly", days = 90)
        val Yearly = PeriodOption(id = "yearly", label = "Yearly", days = 365)

        val all = listOf(Weekly, Monthly, Quarterly, Yearly)
        val default = Monthly

        fun findById(id: String): PeriodOption = all.find { it.id == id } ?: default
    }

    sealed interface Event {
        data object OnLoad : Event
        data class OnCategorySelected(val categoryId: String) : Event
        data class OnAmountChanged(val amount: String) : Event
        data class OnPeriodChanged(val periodId: String) : Event
        data class OnNotesChanged(val notes: String) : Event
        data object OnSave : Event
        data object OnCancel : Event
    }

    sealed interface SideEffect {
        data object NavigateBack : SideEffect
        data class ShowError(val message: String) : SideEffect
        data class ShowSuccess(val message: String) : SideEffect
    }
}
