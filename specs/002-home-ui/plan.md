# Implementation Plan: Home UI

**Branch**: `002-home-ui` | **Date**: 2026-06-08 | **Spec**: [spec.md](spec.md)

**Input**: Feature specification from `/specs/002-home-ui/spec.md`

## Summary

Implement the Home screen UI based on the Figma design using Jetpack Compose and MVI architecture. The UI includes the Total Balance, Add/Transfer actions, a Spending Analysis list, and a Recent Transactions list. Data will initially be mocked to focus on aligning the UI with Figma (including English/Arabic RTL support), then wired up to the local Room database.

## Technical Context

**Language/Version**: Kotlin

**Primary Dependencies**: Jetpack Compose, ViewModel

**Storage**: Room (Offline-first, using mock data initially as per clarification)

**Testing**: JUnit, Compose Test rules (using Fakes)

**Target Platform**: Android (Minimum SDK as per project)

**Project Type**: Android Application

**Performance Goals**: <500ms to load and render cached data

**Constraints**: Strict adherence to Figma UI (typography, spacing) and RTL layout for Arabic

**Scale/Scope**: Single screen with 4 main sub-components

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] Does it follow Clean/Hexagonal Architecture with proper module boundaries?
- [x] Is it Offline-First (Room as SSOT + Sync)?
- [x] Are there 3 distinct implementation solutions proposed? (See `research.md`)
- [x] Does it use Fakes instead of Mocks for testing?
- [x] Is the design production-ready with zero tech debt?

## Project Structure

### Documentation (this feature)

```text
specs/002-home-ui/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
└── tasks.md
```

### Source Code (repository root)

```text
app/
└── src/main/java/com/aarundn/personalfinancetracker/
    └── ui/
        └── home/
            ├── components/
            │   ├── BalanceCard.kt
            │   ├── QuickActionRow.kt
            │   ├── TimeframeToggle.kt
            │   ├── SpendingAnalysisSection.kt
            │   └── TransactionsList.kt
            ├── HomeScreen.kt
            ├── HomeViewModel.kt
            └── HomeContract.kt
```

**Structure Decision**: The implementation focuses on the presentation layer within the `app` module, grouping components under `ui/home` to follow feature-based cohesion. MVI state, intents, and effects will be in `HomeContract.kt`.
