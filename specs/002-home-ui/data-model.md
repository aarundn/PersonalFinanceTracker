# Data Model: Home UI

This document outlines the UI-specific entities and intents for the Home screen, updated to match the Figma design specifically. Since the implementation focuses on the UI first using mock data, these represent presentation-layer models.

## Entities (UI Models)

### `HomeUiState`
```kotlin
data class HomeUiState(
    val isLoading: Boolean = true,
    val totalBalance: String = "٠ ريال",
    val balancePercentageChange: String = "٠٪", // e.g., ٤.٥٪
    val selectedAnalysisTimeframe: Timeframe = Timeframe.MONTHLY,
    val spendingAnalysisData: List<SpendingItemUiModel> = emptyList(),
    val recentTransactions: List<TransactionUiModel> = emptyList(),
    val errorMessage: String? = null
)

enum class Timeframe {
    WEEKLY, MONTHLY
}
```

### `SpendingItemUiModel`
```kotlin
data class SpendingItemUiModel(
    val categoryName: String,
    val amount: String // e.g., ٢٠٠ ر.س
)
```

### `TransactionUiModel`
```kotlin
data class TransactionUiModel(
    val id: String,
    val title: String,
    val amount: String,
    val date: String,
    val isIncome: Boolean,
    val categoryIconUrl: String?
)
```

## Intents (MVI Actions)

### `HomeIntent`
```kotlin
sealed interface HomeIntent {
    object LoadHomeData : HomeIntent
    object AddTransactionClicked : HomeIntent // إضافة (No-op initially)
    object TransferClicked : HomeIntent       // تحويل (No-op initially)
    data class AnalysisTimeframeChanged(val timeframe: Timeframe) : HomeIntent
    data class TransactionClicked(val id: String) : HomeIntent
}
```

## Validation & Formatting Rules
- **Localization**: Amounts must be formatted using the string resources `values/strings.xml` and `values-ar/strings.xml`.
- **RTL Support**: The layout must strictly follow bidirectional layout rules. Use `Start`/`End` padding instead of `Left`/`Right`.
