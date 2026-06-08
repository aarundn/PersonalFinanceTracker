# Feature Specification: Home UI

**Feature Branch**: `002-home-ui`

**Created**: 2026-06-08

**Status**: Draft

**Input**: User description: "i want you now to do the home ui based on the figma desing in this link https://www.figma.com/design/kTEXhRZpaJ7OELu0jTa5WW/Personal-tracker-app-ui?node-id=1-216&t=zVngdgspPS0NEA7p-4 followin the same style and using the shared components in clean way"

## Clarifications

### Session 2026-06-08
- Q: Navigation for Add/Transfer Actions → A: Do not handle the click events at all for now
- Q: Spending Analysis Visualization → A: Visualized as a vertical list matching the Figma UI
- Q: Data Source Integration → A: Use mock data only for now to focus on UI alignment

## User Scenarios & Testing *(mandatory)*

### User Story 1 - View Financial Summary (Priority: P1)

As a user, I want to see my total balance (إجمالي الرصيد) prominently displayed on the home screen so I can quickly gauge my overall financial health.

**Why this priority**: Core value of the app is tracking finances; the total balance is the primary metric.

**Independent Test**: Can be fully tested by verifying that the balance card displays the correctly aggregated data with the currency (ريال).

**Acceptance Scenarios**:

1. **Given** the user has logged in and has transactions, **When** they navigate to the home screen, **Then** the total balance is displayed with the correct currency formatting (e.g., ٤٢,٥٠٠ ريال).

---

### User Story 2 - Quick Actions (Add/Transfer) (Priority: P1)

As a user, I want quick access to core actions like "Add" (إضافة) and "Transfer" (تحويل) directly from the summary card.

**Why this priority**: Reduces friction for recording transactions, which is the primary repetitive user flow.

**Independent Test**: Verify that tapping the Add and Transfer buttons correctly fires the respective intents or navigation events.

**Acceptance Scenarios**:

1. **Given** the user is on the home screen, **When** they tap "إضافة" or "تحويل", **Then** nothing happens (click events are unhandled for now, pending future screens).

---

### User Story 3 - Spending Analysis (Priority: P2)

As a user, I want to see a Spending Analysis (تحليل الإنفاق) with the ability to toggle between Weekly (أسبوعي) and Monthly (شهري) views.

**Why this priority**: Helps users understand their spending habits over specific timeframes.

**Independent Test**: Verify that toggling the timeframe correctly updates the spending data displayed below it.

**Acceptance Scenarios**:

1. **Given** the user is viewing the home screen, **When** they toggle to "شهري", **Then** the spending analysis updates to show a vertical list of monthly data matching the Figma UI (e.g., categories with amounts).

---

### User Story 4 - View Recent Transactions (Priority: P2)

As a user, I want to see a list of my recent transactions (المعاملات) on the home screen so I can quickly verify recent activity.

**Why this priority**: Provides context and reassurance that entries were saved.

**Acceptance Scenarios**:

1. **Given** the user has transactions, **When** viewing the home screen, **Then** a list of recent transactions is visible below the spending analysis.

### Edge Cases

- What happens if a user has extremely large numbers for their balance? (Scale text appropriately).
- How does the UI handle zero states if no data is present? (Show 0 balance and empty charts).

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST display the Total Balance (إجمالي الرصيد).
- **FR-002**: System MUST display action buttons for Add (إضافة) and Transfer (تحويل).
- **FR-003**: System MUST display a Spending Analysis (تحليل الإنفاق) section with Weekly/Monthly toggle chips.
- **FR-004**: System MUST display a Recent Transactions (المعاملات) list.
- **FR-005**: System MUST support bidirectional layout (LTR for English, RTL for Arabic). The UI structure will match the Figma layout structurally but follow the project's standard layout directions based on the locale.
- **FR-006**: System MUST use existing shared components (colors, typography).
- **FR-007**: System MUST provide texts as externalized string resources in both English (default) and Arabic (e.g., `values/strings.xml` and `values-ar/strings.xml`).

### Key Entities *(include if feature involves data)*

- **Transaction**: Amount, category, date, type.
- **Account**: Balance.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 100% adherence to RTL layout and Arabic typography as specified in Figma.
- **SC-002**: The home screen loads and renders cached data in under 500ms on an average device.

## Assumptions

- We assume the Arabic font (IBM Plex Sans Arabic) is available in the project resources.
- We assume the project is configured for RTL support.
