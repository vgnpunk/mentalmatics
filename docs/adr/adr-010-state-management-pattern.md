# ADR-010: State Management Pattern

**Status:** Decided
**Date:** Planning phase

## Context

The Compose Multiplatform UI (ADR-001) needs a consistent
state-management pattern (e.g. for practice sessions, timers,
settings).

To clarify: e.g. MVI vs. MVVM with Compose state hoisting.

## Decision

**Unidirectional data flow with StateFlow view models**, without an
external MVI framework: each screen has a `ViewModel`
(`androidx.lifecycle.viewmodel`, multiplatform-capable since 2024) with
a single `StateFlow<UiState>` and a function for user intents
(`onEvent(Event)`); one-shot effects (e.g. navigation) run through a
separate `Channel`/`SharedFlow`. This makes the pattern effectively
MVI-like (one state, explicit events), but without an extra library
dependency (simplicity goal).

## Alternatives

- **A full MVI framework** (e.g. Orbit, Tivi-MVI): an extra dependency
  with no clear benefit over a lean, hand-written StateFlow pattern at
  this project's scope.
- **Pure Compose state hoisting without a view model**: for session
  state with a timer and multiple steps (task → answer → evaluation),
  harder to test and more fragile across recompositions/process death.

## Consequences

- `UiState` data classes and `Event` sealed classes per screen are
  well isolated and unit-testable (ADR-008), without a Compose test
  environment.
- View models are provided via Koin (ADR-007) and obtain repositories
  from `:data`/`:core`.
