package com.example.core.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel providing MVI infrastructure for transaction-related screens.
 * 
 * This class ONLY handles:
 * - State management (MutableStateFlow)
 * - Side effects (Channel-based one-time events)
 */
abstract class BaseViewModel<
        S : MVIState,
        E : MVIUiEvent,
        SE : MVIUiSideEffect> : ViewModel() {

    protected val _uiState: MutableStateFlow<S> by lazy { MutableStateFlow(createInitialState()) }
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    protected val _sideEffect: Channel<SE> = Channel(Channel.CONFLATED)
    val sideEffect: Flow<SE> = _sideEffect.receiveAsFlow()

    /**
     * Must be implemented by subclasses to provide the initial state.
     */
    protected abstract fun createInitialState(): S

    /**
     * Public function for the UI to send events to the ViewModel.
     */
    fun onEvent(event: E) {
        handleEvent(event)
    }

    /**
     * Must be implemented by subclasses to handle specific UiEvents.
     * This is where the logic for processing events and updating state or triggering side effects resides.
     */
    protected abstract fun handleEvent(event: E)

    /**
     * Helper function to update the UiState.
     * It should be called from within viewModelScope or another coroutine.
     */
    protected fun setState(reducer: S.() -> S) {
        _uiState.value = uiState.value.reducer()
    }

    /**
     * Helper function to trigger a UiSideEffect.
     * It launches a new coroutine in viewModelScope to send the effect.
     */
    protected fun triggerSideEffect(effect: SE) {
        viewModelScope.launch { _sideEffect.send(effect) }
    }
}