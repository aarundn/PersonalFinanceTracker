# Research: Home UI Implementation Approaches

## 3 Distinct Implementation Solutions

### Solution 1: Standard MVI with Mocked State in ViewModel (Chosen)
**Decision**: We will use a strict MVI (Model-View-Intent) pattern where a single `HomeUiState` data class represents the entire screen. The `HomeViewModel` handles intents (e.g., `LoadHomeData`, `AnalysisTimeframeChanged`) and exposes a `StateFlow<HomeUiState>`. Initially, the ViewModel will emit hardcoded mock data to strictly focus on Figma UI alignment.
**Rationale**: This aligns perfectly with the project's standard Jetpack Compose architecture. Using mock data initially (as clarified in the spec) unblocks UI development from backend/DB readiness, ensuring we can achieve 100% adherence to the Figma design before wiring real data.
**Alternatives considered**: 
- Direct UI mocking without a ViewModel (rejected as it doesn't represent real-world use).
- Immediate Room DB integration (rejected per user clarification to focus on UI first).

### Solution 2: Modular Composables with Scoped State
**Decision**: Breaking down the screen into entirely independent composables that each manage their own state (e.g., `SpendingAnalysisSection` fetches its own data independently).
**Rationale**: While this reduces the complexity of a massive global state object, it violates the unidirectional data flow standard in MVI where the Screen ViewModel is the single source of truth.
**Status**: Rejected.

### Solution 3: Direct Room Database Observation in UI
**Decision**: Using Compose `collectAsState()` directly on Room Flow queries without a ViewModel intermediary.
**Rationale**: Extremely fast to implement, but violates Clean Architecture principles by coupling the presentation layer directly to the data layer. It also makes mocking the UI (as requested) significantly harder.
**Status**: Rejected.

## Dependencies & Best Practices
- **Jetpack Compose**: Use `@Preview(locale = "ar")` to ensure RTL layout behaves correctly.
- **Typography**: Verify `IBM Plex Sans Arabic` is loaded via `FontFamily` in `Type.kt` and applied to `MaterialTheme.typography`.
