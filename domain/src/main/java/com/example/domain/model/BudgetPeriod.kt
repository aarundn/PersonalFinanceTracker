package com.example.domain.model

enum class BudgetPeriod(val id: String, val label: String, val days: Int) {
    Weekly("weekly", "Weekly", 7),
    Monthly("monthly", "Monthly", 30),
    Quarterly("quarterly", "Quarterly", 90),
    Yearly("yearly", "Yearly", 365);

    companion object {
        val default = Monthly
        fun fromId(id: String): BudgetPeriod = entries.find { it.id == id } ?: default
    }
}
