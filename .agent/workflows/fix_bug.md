---
description: How to systematically investigate and fix bugs
---

# Fix Bug Workflow

## 1. Reproduce & Understand

- Read the error message / stack trace carefully
- Identify which layer the bug originates from:
  - **Crash in UI?** → Check Screen/Route composables
  - **Wrong data displayed?** → Check ViewModel state mapping, mappers
  - **Data not loading?** → Check use case, repository, DAO, database
  - **Navigation broken?** → Check FeatureImpl, Routes, Navigation.kt
  - **DI error?** → Check Koin module registration

## 2. Trace the Data Flow

Follow the data flow in this order:
1. **DAO** → Is the query correct?
2. **Repository** → Is the mapping correct?
3. **Use Case** → Is it passing the right params?
4. **ViewModel** → Is the Flow chain correct? Check `.catch`, `.map`, `.flatMapLatest`
5. **Route** → Is `collectAsStateWithLifecycle()` used? Is `LaunchedEffect` correct?
6. **Screen** → Is it handling all UiState variants (Loading, Success, Error)?

## 3. Fix Following Patterns

- **NEVER** add quick hacks — follow the established patterns
- If the fix requires a new use case, follow `/add_use_case` workflow
- If the fix requires a new screen, follow `/add_screen` workflow
- Run the Dart/Kotlin analyzer after fixing to confirm no new issues

## 4. Verify

- Check that the fix doesn't break other features
- Verify module boundaries aren't violated
- Ensure DI is properly wired

## Common Bug Patterns in This Project

| Symptom | Likely Cause | Fix |
|---------|-------------|-----|
| `KoinNullParameterException` | Missing DI registration | Add to appropriate Koin module |
| Recomposition loop | Mutable state in composable | Move state to ViewModel |
| Data not updating | Missing `.map { it.toDomain() }` | Check mappers in repository |
| Navigation crash | Wrong route type | Check `@Serializable` route matches `composable<>` |
| Stale data | Using `collectAsState()` | Switch to `collectAsStateWithLifecycle()` |
| Side effect fires multiple times | Using `SharedFlow` without `collectLatest` | Use `collectLatest` in `LaunchedEffect` |
