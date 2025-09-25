package com.example.personalfinancetracker.features.home

import androidx.lifecycle.ViewModel
import com.example.personalfinancetracker.features.home.HomeContract.Event
import com.example.personalfinancetracker.features.home.HomeContract.SideEffect
import com.example.personalfinancetracker.features.home.HomeContract.State
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class HomeViewModel(
    private val repository: HomeRepository
) : ViewModel() {
    val state = MutableStateFlow(initialState())

    private val _effects = MutableSharedFlow<SideEffect>(extraBufferCapacity = 8)
    val effects = _effects

    fun onEvent(event: Event) {
        when (event) {
            Event.OnClickAddExpense -> emit(SideEffect.NavigateAddExpense)
            Event.OnClickAddIncome -> emit(SideEffect.NavigateAddIncome)
            Event.OnClickBudgets -> emit(SideEffect.NavigateBudgets)
            Event.OnClickTransactions -> emit(SideEffect.NavigateTransactions)
            Event.OnClickCurrency -> emit(SideEffect.NavigateCurrency)
            is Event.OnClickBudgetItem -> emit(SideEffect.ShowMessage("${event.name} tapped"))
            Event.OnRetry -> Unit
        }
    }

    private fun emit(effect: SideEffect) { _effects.tryEmit(effect) }

    private fun initialState(): State = repository.initialHomeState()
}

