# Core Shared Utilities

The `:core` module provides cross-cutting abstractions that all other modules depend on. It contains **no business logic** — only shared contracts, UI models, and infrastructure utilities.

## `UiText` — Framework-Free Text for ViewModels

ViewModels need to produce user-facing text but should not depend on Android `Context`. `UiText` is a sealed class that bridges this gap:

```kotlin
sealed class UiText {
    data class DynamicString(val value: String) : UiText()

    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : UiText()

    @Composable
    fun asString(): String = when (this) {
        is DynamicString -> value
        is StringResource -> stringResource(id = resId, formatArgs = args)
    }

    fun asString(context: Context): String = when (this) {
        is DynamicString -> value
        is StringResource -> context.getString(resId, *args)
    }
}
```

**When to use which variant:**
| Variant | Use For |
|---|---|
| `DynamicString` | Runtime values from network responses, user input, computed strings |
| `StringResource` | All static/user-facing text that requires localization |

**Example:**
```kotlin
// In ViewModel
triggerSideEffect(ShowError(UiText.StringResource(R.string.error_invalid_amount)))

// In Composable
Text(text = sideEffect.message.asString())
```

## `CoroutineDispatchers` — Injectable Dispatchers

A simple data class that wraps the standard dispatchers, making them injectable and swappable in tests:

```kotlin
data class CoroutineDispatchers(
    val main: CoroutineDispatcher,
    val io: CoroutineDispatcher,
    val default: CoroutineDispatcher
)
```

**Production (via `coreModule`):**
```kotlin
single {
    CoroutineDispatchers(
        main = Dispatchers.Main,
        io = Dispatchers.IO,
        default = Dispatchers.Default
    )
}
```

**Testing:**
```kotlin
val testDispatchers = CoroutineDispatchers(
    main = StandardTestDispatcher(),
    io = StandardTestDispatcher(),
    default = StandardTestDispatcher()
)
```

## `DefaultCurrencies` & `DefaultCategories`

Static reference data providing currency and category metadata.

### `DefaultCurrencies`

Provides a list of supported currencies with their symbols:

```kotlin
DefaultCurrencies.fromId("USD")?.symbol  // "$"
DefaultCurrencies.fromId("EUR")?.symbol  // "€"
```

Used by UI models to resolve `currencySymbol` from a currency code.

### `DefaultCategories`

Provides a list of transaction/budget categories with icons and colors:

```kotlin
DefaultCategories.fromId("food")?.icon       // Icon resource
DefaultCategories.fromId("food")?.color      // Color value
DefaultCategories.fromId("food")?.nameResId  // String resource ID
```

Used by `TransactionUi` and `BudgetUi` to resolve `currentCategory`.

## `BudgetDisplayData`

A presentation-layer data class for rendering budget cards. Created via `BudgetUi.toDisplayData()`:

```kotlin
data class BudgetDisplayData(
    val iconTint: Color,
    val iconBackground: Color,
    val icon: ImageVector,
    val categoryName: Int,       // @StringRes
    val currencySymbol: String,
    val spent: Double,
    val amount: Double,
    val isOverBudget: Boolean,
    val isWarning: Boolean,
    val overBudget: Double,
    val remaining: Double
)
```

## Navigation Contracts

### `Feature` Interface

The base contract for modular navigation. See [Navigation](../architecture/04_navigation.md) for details.

```kotlin
interface Feature {
    fun registerGraph(navGraphBuilder: NavGraphBuilder, navController: NavController, modifier: Modifier)
}

interface AppRoutes  // Marker for type-safe route objects
```

### Feature-Specific Interfaces

Defined in `core/navigation/features/`:

| Interface | Route Accessors |
|---|---|
| `HomeFeature` | `homeRoute()` |
| `TransactionFeature` | `transactionsRoute()`, `addTransactionRoute()`, `editTransactionRoute(id)` |
| `SettingsFeature` | `settingsRoute()` |
| `BudgetFeature` | `budgetRoute()`, `addBudgetRoute()` |
