# Tasks: Refactor Color System

**Input**: Design documents from `specs/001-refactor-color-system/`

**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, quickstart.md

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [x] T001 Verify/Create directory structure `core/src/main/java/com/aarundn/personalfinancetracker/core/presentation/theme/`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [x] T002 Implement `AppColorTokens` object containing the extracted base colors in `core/src/main/java/com/example/core/ui/theme/Color.kt`
- [x] T003 Implement `AppAlphaTokens` object containing the alpha constants in `core/src/main/java/com/example/core/ui/theme/Color.kt`

**Checkpoint**: Foundation ready - user story implementation can now begin

---

## Phase 3: User Story 1 - Centralized Color Palette (Priority: P1) 🎯 MVP

**Goal**: Implement a clean, unified color system derived from the Figma design, incorporating alpha variants properly, so the application theme remains consistent, modern, and easy to maintain.

**Independent Test**: Can be verified by reviewing the updated theme definitions and verifying the app's visual appearance matches the Figma design without breaking the existing theme.

### Implementation for User Story 1

- [x] T004 [US1] Map base color tokens to Material 3 `LightColorScheme` in `core/src/main/java/com/example/core/ui/theme/Theme.kt`
- [x] T005 [US1] Map base color tokens to Material 3 `DarkColorScheme` in `core/src/main/java/com/example/core/ui/theme/Theme.kt`
- [x] T006 [US1] Define standard semantic alpha usage helpers or extensions (e.g., `Color.withHoverAlpha()`) in `core/src/main/java/com/example/core/ui/theme/Color.kt`

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase N: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [ ] T007 Code cleanup and refactoring: Replace scattered hardcoded hex codes across UI components to use the new tokens
- [ ] T008 Validate visual appearance matches Figma without breaking the app theme

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3+)**: All depend on Foundational phase completion
- **Polish (Final Phase)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational (Phase 2) - No dependencies on other stories

### Within Each User Story

- Core implementation before integration
- Story complete before moving to next priority

### Parallel Opportunities

- Due to the nature of this refactoring modifying the same few files (`Color.kt` and `Theme.kt`), most tasks should be executed sequentially. However, UI component cleanup (T007) could potentially be parallelized across different screens once the theme is established.

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational (CRITICAL - blocks all stories)
3. Complete Phase 3: User Story 1
4. **STOP and VALIDATE**: Test User Story 1 independently
5. Deploy/demo if ready

### Incremental Delivery

1. Complete Setup + Foundational → Foundation ready
2. Add User Story 1 → Test independently → Deploy/Demo (MVP!)
