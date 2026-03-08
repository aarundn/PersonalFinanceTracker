package com.example.core.common

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * A sealed class that represents UI text that can come from either
 * a hardcoded dynamic string or an Android string resource.
 *
 * This allows ViewModels and Domain-layer classes to pass text without
 * needing an Android Context, while still supporting full localization.
 *
 * Usage in a ViewModel:
 *   triggerSideEffect(ShowError(UiText.StringResource(R.string.error_budget_not_found)))
 *
 * Usage in a Composable:
 *   Text(uiText.asString())
 *
 * Usage in non-Composable code (e.g. a snackbar callback):
 *   uiText.asString(context)
 */
sealed class UiText {

    /** A plain runtime string — use only for dynamic values (e.g. from a network response). */
    data class DynamicString(val value: String) : UiText()

    /** A string resource reference — use for all static/user-facing text. */
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : UiText()

    /** Resolve the text inside a Composable. */
    @Composable
    fun asString(): String = when (this) {
        is DynamicString -> value
        is StringResource -> stringResource(id = resId, formatArgs = args)
    }

    /** Resolve the text outside a Composable (e.g. in a SnackBar callback). */
    fun asString(context: Context): String = when (this) {
        is DynamicString -> value
        is StringResource -> context.getString(resId, *args)
    }
}
