# ADR-011: Navigation Solution

**Status:** Decided
**Date:** Planning phase

## Context

The app needs navigation between exercise areas, settings, the Tips &
Tricks section, etc., cross-platform with Compose Multiplatform.

To clarify: e.g. Compose Multiplatform Navigation (official) vs.
Voyager vs. Decompose.

## Decision

**The official androidx.navigation Compose Multiplatform library**
(JetBrains/Google, type-safe routes). As the official,
JetBrains-backed solution, it carries the least dependency on
individual third-party maintainers and integrates best with the rest
of the Compose Multiplatform toolchain (ADR-001).

## Alternatives

- **Voyager**: simple API, good KMP support, but a community project
  with less certain long-term maintenance compared to the official
  solution.
- **Decompose**: more powerful (its own lifecycle/state concept), but
  unnecessarily complex for this app's modest navigation needs (few
  screens, flat hierarchy) — contradicts the simplicity goal.

## Consequences

- The navigation graph lives in the `:ui` module (ADR-009), type-safe
  routes as `@Serializable` classes per screen.
- Navigation events are triggered as one-shot effects from the view
  model per ADR-010, not directly from the UI layer.
