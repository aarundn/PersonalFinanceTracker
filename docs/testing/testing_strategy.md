# Testing Strategy

This document covers the testing philosophy, patterns, and conventions used across the project.

## Testing Philosophy

> **Fakes over Mocks.** We prefer handwritten `Fake` implementations over mocking frameworks (Mockito, MockK) for test doubles.

**Why Fakes?**
- Fakes replicate **real behavior** (in-memory lists, actual `MutableStateFlow` emissions) — mocks only verify method calls.
- Tests are **not tied to implementation details** — refactoring internal logic won't break tests.
- Fakes are **reusable** across all tests that depend on the same interface.
- Fakes make tests **read like specifications** — you set up state, perform an action, and verify the result.

## Test Architecture

### Data Module Tests (`data/src/test/`)

Focus on the **mapping pipeline** — ensuring data integrity across all three layers:

| Test File | What It Tests |
|---|---|
| `TransactionMapperTest` | `Transaction` ↔ `TransactionEntity`, `TransactionEntity` ↔ `TransactionsDto` |
| `BudgetMapperTest` | `Budget` ↔ `BudgetEntity`, `BudgetEntity` ↔ `BudgetDto` |
| `CategoryMapperTest` | `Category` ↔ `CategoryEntity` |

### App Module Tests (`app/src/test/`)

Focus on **ViewModel behavior** and **UI-layer mapping**:

| Test File | What It Tests |
|---|---|
| `HomeViewModelTest` | State initialization, data aggregation, budget calculations |
| `AddTransactionViewModelTest` | Form validation, save flow, error handling |
| `AddBudgetViewModelTest` | Form validation, save flow, error handling |
| `SettingsViewModelTest` | Preferences, provider selection, conversion, sync init |
| `TransactionMapperTest` (app) | `Transaction` ↔ `TransactionUi` |
| `BudgetMapperTest` (app) | `Budget` ↔ `BudgetUi`, `BudgetUi.toDisplayData()` |

## Test Doubles (Fakes)

All fakes live in `app/src/test/java/.../fakes/`:

| Fake | Implements | Key Behavior |
|---|---|---|
| `FakeTransactionRepository` | `TransactionRepository` | In-memory `MutableList`, returns data via `Flow` |
| `FakeBudgetRepository` | `BudgetRepository` | In-memory `MutableList`, returns data via `Flow` |
| `FakeUserPreferencesRepository` | `UserPreferencesRepository` | In-memory currency preference |
| `FakeExchangeRateRepository` | `ExchangeRateRepository` | Configurable conversion results |
| `FakeRateSyncManager` | `RateSyncManager` | Records calls for assertion |

## ViewModel Testing Pattern

Every ViewModel test follows this structure:

### 1. Setup

```kotlin
class HomeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var transactionRepository: FakeTransactionRepository
    private lateinit var budgetRepository: FakeBudgetRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        transactionRepository = FakeTransactionRepository()
        budgetRepository = FakeBudgetRepository()
        viewModel = HomeViewModel(transactionRepository, budgetRepository)
    }
}
```

### 2. `MainDispatcherRule`

Replaces `Dispatchers.Main` with a `StandardTestDispatcher` so coroutines execute deterministically:

```kotlin
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }
    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
```

### 3. Testing State with Turbine

[Turbine](https://github.com/cashapp/turbine) is used to test `Flow` emissions. It provides `awaitItem()` to assert each emission:

```kotlin
@Test
fun `given valid data, when init, then state has correct totals`() = runTest {
    transactionRepository.addTransaction(testTransaction)

    viewModel = HomeViewModel(transactionRepository, budgetRepository)
    advanceUntilIdle()

    viewModel.uiState.test {
        val state = awaitItem()
        assertEquals(100.0, state.totalExpenses, 0.0)
        cancelAndIgnoreRemainingEvents()
    }
}
```

### 4. Testing Side Effects with Turbine

Side effects (navigation, snackbars) are tested by collecting from the `sideEffect` Flow:

```kotlin
@Test
fun `given valid form, when save clicked, then NavigateBack is emitted`() = runTest {
    viewModel.sideEffect.test {
        viewModel.onEvent(OnSaveClicked)
        advanceUntilIdle()

        val effect = awaitItem()
        assertTrue(effect is NavigateBack)
        cancelAndIgnoreRemainingEvents()
    }
}
```

### 5. Key Patterns

| Pattern | Purpose |
|---|---|
| `advanceUntilIdle()` | Executes all pending coroutines in the test dispatcher |
| `awaitItem()` | Blocks until the next emission and returns it |
| `cancelAndIgnoreRemainingEvents()` | Cleans up Turbine after assertions |
| `runTest { }` | Provides a test coroutine scope |

## Naming Convention

All test methods use descriptive backtick names:

```kotlin
@Test
fun `given empty amount, when save clicked, then show validation error`()

@Test
fun `given network failure, when sync triggered, then state has error`()
```

Format: `` `given [precondition], when [action], then [expected outcome]` ``

## Coverage Targets

Each ViewModel test suite should cover:

- **Initialization:** Default state, data loading from repositories.
- **All Event Handlers:** Every `Event` type in the contract.
- **Success Paths:** Happy-path flows from event → state update → side effect.
- **Error Paths:** Network failures, validation errors, missing data.
- **Edge Cases:** Empty lists, invalid input, same-currency conversion, missing providers.
