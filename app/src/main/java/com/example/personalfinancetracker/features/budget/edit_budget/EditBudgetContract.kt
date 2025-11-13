package com.example.personalfinancetracker.features.budget.edit_budget

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * UDF contract for the Edit Budget feature.
 */
object EditBudgetContract {

    @Immutable
    data class State(
        val budgetId: Int = -1,
        val category: String = "",
        val budgetedAmountInput: String = "",
        val budgetedAmountOriginal: Double = 0.0,
        val spentAmount: Double = 0.0,
        val periodInput: String = PeriodOptions.default.id,
        val periodOriginal: String = PeriodOptions.default.id,
        val notesInput: String = "",
        val notesOriginal: String = "",
        val icon: ImageVector? = null,
        val iconTint: Color = Color.Unspecified,
        val iconBackground: Color = Color.Transparent,
        val isEditing: Boolean = false,
        val isLoading: Boolean = false,
        val error: String? = null,
        val daysElapsed: Int = 0,
        val daysInPeriod: Int = PeriodOptions.default.days,
        val showDeleteDialog: Boolean = false
    ) {
        val budgetedAmount: Double
            get() = budgetedAmountInput.toDoubleOrNull() ?: budgetedAmountOriginal

        val percentageUsed: Float
            get() = if (budgetedAmount <= 0.0) 0f else (spentAmount / budgetedAmount).toFloat()

        val remainingAmount: Double
            get() = budgetedAmount - spentAmount

        val isOverBudget: Boolean
            get() = remainingAmount < 0

        val isWarning: Boolean
            get() = !isOverBudget && percentageUsed >= 0.8f

        val averageDailySpend: Double
            get() = if (daysElapsed > 0) spentAmount / daysElapsed else 0.0

        val projectedTotal: Double
            get() = if (daysElapsed > 0) averageDailySpend * daysInPeriod else spentAmount
    }

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
        data object OnLoadBudget : Event
        data object OnEdit : Event
        data object OnCancel : Event
        data object OnCancelEdit : Event
        data object OnSave : Event
        data object OnDelete : Event
        data object OnConfirmDelete : Event
        data object OnDismissDeleteDialog : Event
        data class OnBudgetAmountChanged(val amount: String) : Event
        data class OnPeriodChanged(val periodId: String) : Event
        data class OnNotesChanged(val notes: String) : Event
    }

    sealed interface SideEffect {
        data object NavigateBack : SideEffect
        data class ShowSuccess(val message: String) : SideEffect
        data class ShowError(val message: String) : SideEffect
    }
}
