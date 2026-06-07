# Research: Color System Refactoring

## Decision: Approach for Managing Alpha Variants in Compose

**Decision**: **Approach 3: Tokenized Object Pattern combined with Semantic Mapping**

**Rationale**: 
The extracted Figma colors show a clear pattern: a base color (e.g., Blue `3B82F6`, Violet `8B5CF6`, Emerald `10B981`) combined with specific alpha opacities (e.g., 0%, 10%, 15%, 25%, 50%). Hardcoding these combinations as distinct flat colors (e.g., `Blue15Alpha`) leads to a bloated color palette and reduces maintainability. By separating the base colors (Color Tokens) and the alpha values (Alpha Tokens), we perfectly mirror the "pro" design system used in Figma. We then map these combinations into our Material 3 `ColorScheme` (or an extended `CompositionLocal` for custom semantic roles) using the built-in `Color.copy(alpha = ...)` function. This maintains the app theme's integrity while allowing a clean, scalable application of transparency.

**Alternatives Considered**:
1. **Approach 1: Flat Custom Color Roles (Rejected)**
   *   *Description*: Create dedicated variables for each combination (e.g., `val PrimaryTransparent = Color(0x263B82F6)`).
   *   *Reason for Rejection*: Does not scale well. If the base primary color changes, the developer must remember to manually recalculate and update the hex codes for all transparent variants.

2. **Approach 2: Inline `.copy(alpha = 0.15f)` usage (Rejected)**
   *   *Description*: Let developers use `.copy()` with raw float values anywhere in the UI code.
   *   *Reason for Rejection*: Violates consistency. Developers might use `0.15f` in one place and `0.20f` in another for the same semantic state (like hover or disabled).

3. **Approach 3: Tokenized Object Pattern (Selected)**
   *   *Description*: Define `val Blue500 = Color(0xFF3B82F6)`, `val AlphaHover = 0.15f`. Then define `val PrimaryHover = Blue500.copy(alpha = AlphaHover)`.
   *   *Reason for Selection*: Extremely clean, avoids hardcoded hex combinations, updates automatically if base colors change, and strictly enforces the Figma alpha scale.

## Implementation Details
Based on the JSON extraction from `figma_data_utf8.json`, the following tokens are identified:

### Base Color Tokens
- `Emerald500` = `#10B981`
- `Blue500` = `#3B82F6`
- `Violet500` = `#8B5CF6`
- `Pink500` = `#EC4899`
- `Gray500` = `#6B7280`
- `Gray600` = `#4B5563`
- `Gray800` = `#1F2937`
- `Black` = `#000000`
- `AlmostBlack` = `#050505`
- `DarkSurface` = `#121212`
- `DarkSlate` = `#0C1A31`
- `DarkBackground` = `#030213`
- `White` = `#FFFFFF`

### Alpha Tokens
- `Alpha0` = `0.0f` (0%)
- `Alpha10` = `0.10f` (~10%)
- `Alpha15` = `0.15f` (~15%)
- `Alpha25` = `0.25f` (~25%)
- `Alpha50` = `0.50f` (~50%)

### Mapping to Theme
These tokens will be defined in `core/presentation/theme/Color.kt` and mapped to the standard `MaterialTheme.colorScheme` properties where applicable. Custom semantic combinations will be exposed via a custom `CompositionLocal` or Extension properties on `ColorScheme`.
