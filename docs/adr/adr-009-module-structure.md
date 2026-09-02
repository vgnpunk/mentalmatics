# ADR-009: Module/Project Structure

**Status:** Decided
**Date:** Planning phase

## Context

`docs/architecture.md` (chapter 5, building block view) outlines a
rough module split (core logic, persistence, UI, platform shells,
content). This needs to be finally decided (e.g. Gradle module
structure, feature-based vs. layer-based split) before tickets can be
derived.

## Decision

A **layer-based split** (not feature-based) across Gradle modules,
directly following the outline from `architecture.md` chapter 5.1:

- `:core` — task generation, difficulty logic, number base conversion.
  Pure Kotlin, no Compose/platform dependency.
- `:data` — SQLDelight schemas, repositories for settings and progress
  (ADR-006).
- `:ui` — shared Compose Multiplatform screens, components, navigation
  (ADR-011), state management (ADR-010).
- `:content` — static, localized Tips & Tricks content.
- `:androidApp`, `:iosApp` — thin platform shells (entry points,
  platform-specific wiring).

With three exercise areas that share very similar patterns (selection
→ generation → session → optional tracking), a layer-based split is
easier to maintain for a single-person project than a feature-based
split with three parallel vertical slices.

## Alternatives

- **Feature-based split** (one module per exercise area): discarded,
  since at this project's scope it brings more module overhead than
  benefit, and shared patterns (session handling, tracking) would need
  to be duplicated or extracted into an additional shared module.

## Consequences

- Within `:core` and `:ui`, the split by exercise area happens via
  packages (not modules) (`core.arithmetic`, `core.scenarios`,
  `core.baseconversion`).
- `docs/coding-conventions.md` gets a code-organization section.
- Ticket scoping can follow module boundaries, without ticket A
  breaking module interfaces that ticket B is working on.
