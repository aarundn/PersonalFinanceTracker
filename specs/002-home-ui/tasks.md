---
description: "Task list for Home UI implementation"
---

# Tasks: Home UI

**Input**: Design documents from `/specs/002-home-ui/`

**Prerequisites**: plan.md, spec.md, research.md, data-model.md, quickstart.md

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3, US4)
- Include exact file paths in descriptions

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [x] T001 Verify and setup typography configuration for IBM Plex Sans Arabic in `app/src/main/java/com/aarundn/personalfinancetracker/ui/theme/Type.kt`
- [x] T002 Extract and add English string resources to `app/src/main/res/values/strings.xml` (e.g. `home_total_balance`, `home_action_transfer`, `home_action_add`, etc.)
- [x] T003 Extract and add Arabic string resources to `app/src/main/res/values-ar/strings.xml`
- [x] T004 Define MVI foundational classes (`HomeUiState`, `Timeframe`, `HomeIntent`) in `app/src/main/java/com/aarundn/personalfinancetracker/ui/home/HomeContract.kt` (based on data-model.md)

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

- [x] T005 [P] Create empty `HomeViewModel` extending ViewModel and implementing MVI base in `app/src/main/java/com/aarundn/personalfinancetracker/ui/home/HomeViewModel.kt`
- [x] T006 [P] Create base `HomeScreen` composable Scaffold structure in `app/src/main/java/com/aarundn/personalfinancetracker/ui/home/HomeScreen.kt`
- [x] T007 Integrate `HomeScreen` into the Main Application Navigation in `app/src/main/java/com/aarundn/personalfinancetracker/ui/navigation/AppNavigation.kt`

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - View Financial Summary (Priority: P1) 🎯 MVP

**Goal**: Display the total balance and percentage change prominently on the home screen.

**Independent Test**: Verify that the balance card displays the correctly aggregated data with the currency formatted correctly based on locale (RTL/LTR).

### Implementation for User Story 1

- [x] T008 [P] [US1] Create `BalanceCard` composable in `app/src/main/java/com/aarundn/personalfinancetracker/ui/home/components/BalanceCard.kt`
- [x] T009 [US1] Implement dummy state loading for total balance in `HomeViewModel.kt`
- [x] T010 [US1] Integrate `BalanceCard` into `HomeScreen.kt`

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - Quick Actions (Priority: P1)

**Goal**: Provide quick access to Add and Transfer actions.

**Independent Test**: Verify that tapping the Add and Transfer buttons fire respective intents in the ViewModel, doing nothing for now (no-op) as clarified.

### Implementation for User Story 2

- [x] T011 [P] [US2] Create `QuickActionRow` composable with Add/Transfer buttons in `app/src/main/java/com/aarundn/personalfinancetracker/ui/home/components/QuickActionRow.kt`
- [x] T012 [US2] Handle `AddTransactionClicked` and `TransferClicked` intents as no-ops in `HomeViewModel.kt`
- [x] T013 [US2] Integrate `QuickActionRow` into `HomeScreen.kt` below the BalanceCard

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently

---

## Phase 5: User Story 3 - Spending Analysis (Priority: P2)

**Goal**: Display Spending Analysis with Weekly/Monthly toggle, visualized as a vertical list matching the Figma UI.

**Independent Test**: Verify toggling between Weekly and Monthly updates the vertical list data.

### Implementation for User Story 3

- [x] T014 [P] [US3] Create UI model `SpendingItemUiModel` in `app/src/main/java/com/aarundn/personalfinancetracker/ui/home/HomeContract.kt`
- [x] T015 [P] [US3] Create `TimeframeToggle` composable in `app/src/main/java/com/aarundn/personalfinancetracker/ui/home/components/TimeframeToggle.kt`
- [x] T016 [P] [US3] Create `SpendingAnalysisSection` composable (vertical list) in `app/src/main/java/com/aarundn/personalfinancetracker/ui/home/components/SpendingAnalysisSection.kt`
- [x] T017 [US3] Handle `AnalysisTimeframeChanged` intent and mock data switching in `HomeViewModel.kt`
- [x] T018 [US3] Integrate `SpendingAnalysisSection` into `HomeScreen.kt`

**Checkpoint**: At this point, User Story 3 is testable

---

## Phase 6: User Story 4 - View Recent Transactions (Priority: P2)

**Goal**: Show a list of recent transactions.

**Independent Test**: Verify transactions list renders correctly with RTL text alignment for Arabic.

### Implementation for User Story 4

- [x] T019 [P] [US4] Create UI model `TransactionUiModel` in `app/src/main/java/com/aarundn/personalfinancetracker/ui/home/HomeContract.kt`
- [x] T020 [P] [US4] Create `TransactionsList` composable in `app/src/main/java/com/aarundn/personalfinancetracker/ui/home/components/TransactionsList.kt`
- [x] T021 [US4] Expose mock recent transactions state in `HomeViewModel.kt`
- [x] T022 [US4] Integrate `TransactionsList` into `HomeScreen.kt`

**Checkpoint**: All user stories should now be independently functional

---

## Phase 7: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [x] T023 [P] Verify LTR/RTL support across all components using Android Layout Preview
- [x] T024 Ensure Material 3 ColorScheme tokens are correctly applied everywhere (no hardcoded hex values)
- [x] T025 Format code and clean up imports
- [x] T026 Execute manual scenarios defined in `quickstart.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3-6)**: All depend on Foundational phase completion
  - Can proceed in parallel

### User Story Dependencies

- **User Story 1 & 2 (P1)**: Start after Foundational (Phase 2).
- **User Story 3 & 4 (P2)**: Start after Phase 2.

### Parallel Opportunities

- Creating standalone UI components (`BalanceCard`, `QuickActionRow`, `TimeframeToggle`, `SpendingAnalysisSection`, `TransactionsList`) can all be done in parallel before integration.

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational (CRITICAL - blocks all stories)
3. Complete Phase 3: User Story 1
4. **STOP and VALIDATE**: Test User Story 1 independently

### Incremental Delivery

1. Complete Setup + Foundational → Foundation ready
2. Add User Story 1 → Test independently
3. Add User Story 2 → Test independently
4. Add User Story 3 → Test independently
5. Add User Story 4 → Test independently
