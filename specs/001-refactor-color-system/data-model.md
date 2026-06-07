# Data Model: Color System

This feature refactors the presentation layer's theme. While there are no database entities or domain layer models modified, the "Data Model" for this feature represents the internal structure of the design system tokens.

## Entities

### `AppColorTokens`
Represents the base, fully opaque colors derived directly from Figma.

**Properties**:
- `Emerald500`: `Color` (`#10B981`)
- `Blue500`: `Color` (`#3B82F6`)
- `Violet500`: `Color` (`#8B5CF6`)
- `Pink500`: `Color` (`#EC4899`)
- `Gray500`: `Color` (`#6B7280`)
- `Gray600`: `Color` (`#4B5563`)
- `Gray800`: `Color` (`#1F2937`)
- `DarkSurface`: `Color` (`#121212`)
- `DarkSlate`: `Color` (`#0C1A31`)
- `DarkBackground`: `Color` (`#030213`)
- `AlmostBlack`: `Color` (`#050505`)

### `AppAlphaTokens`
Represents the standard opacity levels used across the app.

**Properties**:
- `Transparent`: `Float` (`0.0f`)
- `Hover`: `Float` (`0.10f` / `0.15f`)
- `Disabled`: `Float` (`0.25f`)
- `Scrim`: `Float` (`0.50f`)

## State Transitions & Validation
- **Validation**: All colors must be instantiated through the base tokens. Hardcoded ARGB values containing alpha inside the hex string (e.g., `0x263B82F6`) are considered invalid. They must be composed dynamically using `ColorTokens.Blue500.copy(alpha = AppAlphaTokens.Hover)`.
