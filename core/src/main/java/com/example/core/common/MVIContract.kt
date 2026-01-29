package com.example.core.common

/**
 * Marker interface for transaction-related UI state.
 * Each feature (Add/Edit) defines its own concrete State data class.
 */
interface MVIState

/**
 * Marker interface for transaction-related UI events.
 * Each feature (Add/Edit) defines its own sealed class of events.
 */
interface MVIUiEvent

/**
 * Marker interface for transaction-related side effects.
 * Each feature (Add/Edit) defines its own sealed class of side effects.
 */
interface MVIUiSideEffect