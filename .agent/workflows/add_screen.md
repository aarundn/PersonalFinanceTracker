---
description: How to add a new screen within an existing feature
---

# Add Screen Workflow

Use this when adding a new sub-screen to an existing feature (e.g., adding an "edit" screen to the Budget feature).

## 1. Create the sub-feature directory

Path: `app/.../features/<feature>/<sub_feature>/`

## 2. Create the Contract

Path: `app/.../features/<feature>/<sub_feature>/<Name>Contract.kt`

```kotlin
@Immutable
sealed interface <Name>UiState {
    data object Loading : <Name>UiState
    data class Success(/* relevant fields */) : <Name>UiState
    data class Error(val message: String) : <Name>UiState
}

sealed interface <Name>Event {
    // User actions specific to this screen
}

sealed interface <Name>SideEffect {
    data object NavigateBack : <Name>SideEffect
    data class ShowError(val message: String) : <Name>SideEffect
}
```

## 3. Create the ViewModel

Path: `app/.../features/<feature>/<sub_feature>/<Name>ViewModel.kt`

Follow the ViewModel pattern from AGENT_RULES.md. Key points:
- Constructor-inject use cases
- StateFlow via `.stateIn()` for reactive state
- SharedFlow for one-time side effects
- Single `onEvent()` function

## 4. Create the Screen (stateless)

Path: `app/.../features/<feature>/<sub_feature>/<Name>Screen.kt`

```kotlin
@Composable
fun <Name>Screen(
    uiState: <Name>UiState,
    onEvent: (<Name>Event) -> Unit,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    // Pure UI based on uiState, sends user actions via onEvent
}
```

## 5. Create the Route (stateful wrapper)

Path: `app/.../features/<feature>/<sub_feature>/<Name>Route.kt`

Follow the Route pattern from AGENT_RULES.md. It connects ViewModel to Screen.

## 6. Create the Navigation extension

Path: `app/.../features/<feature>/<sub_feature>/navigation/Navigation.kt`

```kotlin
fun NavGraphBuilder.<subFeature>Route(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    composable<<Feature>Routes.<Sub>Route> {
        <Name>Route(
            onNavigateBack = onNavigateBack,
            modifier = modifier
        )
    }
}
```

## 7. Add the route to the Routes sealed interface

Path: `app/.../features/<feature>/navigation/<Feature>Routes.kt`

```kotlin
@Serializable
data object <Sub>Route : <Feature>Routes  // or data class if it has params
```

## 8. Register in FeatureImpl

Path: `app/.../features/<feature>/navigation/<Feature>FeatureImpl.kt`

Add `navGraphBuilder.<subFeature>Route(...)` inside `registerGraph()`

## 9. Register ViewModel in DI module

Path: `app/.../features/<feature>/di/<Feature>Module.kt`

```kotlin
viewModelOf(::<Name>ViewModel)
```
