package com.example.core.common

import androidx.lifecycle.ViewModel
import com.example.core.model.Categories
import com.example.domain.model.Type
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

abstract class BaseTransactionViewModel<
        S : BaseTransactionState,
        E : BaseTransactionUiEvent,
        SE : BaseTransactionUiSideEffect> : ViewModel() {

    abstract val _uiState: MutableStateFlow<S>
    val uiState: StateFlow<S> get() = _uiState.asStateFlow()

    private val _uiSideEffect = Channel<SE>()
    val uiSideEffect: Flow<SE> = _uiSideEffect.receiveAsFlow()

    protected fun setState(update: S.() -> S) {
        _uiState.update(update)
    }

    protected suspend fun emitSideEffect(effect: SE) {
        _uiSideEffect.send(effect)
    }

    protected fun getCategoriesForType(isIncome: Boolean): List<Categories> {
        return Categories.forType(if (isIncome) Type.INCOME else Type.EXPENSE)
    }
}