<!--
Sync Impact Report:
Version: 0.0.0 -> 1.0.0
Modified Principles:
  - [PRINCIPLE_1_NAME] -> Clean / Hexagonal Architecture
  - [PRINCIPLE_2_NAME] -> Offline-First Data Strategy
  - [PRINCIPLE_3_NAME] -> Quality Guardian (No Tech Debt)
  - [PRINCIPLE_4_NAME] -> Three-Way Planning Requirement
  - [PRINCIPLE_5_NAME] -> Test-First with Fakes (No Mocks)
Added Sections:
  - Technology Standards
  - Development Workflow
Removed Sections: None
Templates Requiring Updates:
  - .specify/templates/plan-template.md: ✅ updated
  - .specify/templates/spec-template.md: ⚠️ pending
  - .specify/templates/tasks-template.md: ⚠️ pending
Follow-up TODOs: None
-->
# Personal Finance Tracker Constitution
<!-- This Constitution governs all development in the Personal Finance Tracker project -->

## Core Principles

### Clean / Hexagonal Architecture
Strict separation of concerns via Ports and Adapters. The Domain module must be completely independent of frameworks. The App module handles UI (Compose/MVI), and the Data module handles implementation details (Room/Firebase/Ktor). Cross-module dependencies must adhere to the Dependency Rule.

### Offline-First Data Strategy
Use the Local Database (Room) as the Single Source of Truth. Implement robust background sync via WorkManager with Last-Write-Wins conflict resolution. The application MUST function perfectly when offline.

### Quality Guardian (No Tech Debt)
Every change MUST be production-ready. No shortcuts, no TODOs, no tech debt. Code must be scalable, maintainable, and respect module boundaries.

### Three-Way Planning Requirement
Always start with planning mode. For any solution or implementation, you MUST suggest 3 distinct ways or architectural approaches before proceeding with code.

### Test-First with Fakes (No Mocks)
Use Fakes instead of mocking frameworks. Employ JUnit and Turbine for Flow testing. Ensure high coverage for Domain and Data mappers. Test logic without UI dependencies using MainDispatcherRule.

## Technology Standards

- **UI**: Jetpack Compose + Material 3
- **Architecture**: MVI (Model-View-Intent) via custom `BaseViewModel`
- **Dependency Injection**: Koin (multi-module)
- **Async**: Kotlin Coroutines + Flow

## Development Workflow

- **Code Reviewer**: Never write code that violates established patterns. Treat them as law.
- **Mentor Approach**: Explain the "why" behind decisions, not just the "what".

## Governance

All architectural decisions must be documented and agreed upon. The Orchestrator Agent (Senior Android Engineer) enforces these rules. Amendments require documentation, approval, and migration plans if breaking changes occur.

**Version**: 1.0.0 | **Ratified**: 2026-06-06 | **Last Amended**: 2026-06-06
