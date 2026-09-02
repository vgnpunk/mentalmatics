# ADR-015: Implementation of the Progress-Tracking Toggle

**Status:** Decided
**Date:** Planning phase

## Context

Per `docs/functional-requirements.md` (US-6.2), when progress tracking
is disabled it must be ensured that truly no data is captured/stored —
not just that the display is suppressed (see the pitfall in
`docs/architecture.md`, chapter 5.2).

To clarify: the concrete technical implementation (e.g. fully
disabling the write path vs. capturing data but not persisting it).

## Decision

**A hard gate on the write path, not just in the UI.** A central
`ProgressTrackingGate` abstraction in `:core` reads the toggle state
from settings (ADR-006) and is consulted by the session orchestration
**before** a session result is ever passed to the `:data` repository.
When "off", the repository write call is never made in the first
place — progress data is never aggregated in memory or passed on to
`:data` at any point.

Additionally: disabling the toggle only prevents **future** writes;
already-stored progress data remains until the user explicitly removes
it via a separate "delete progress data" action in settings (its own
ticket, see ticket derivation).

## Alternatives

- **Keep capturing data but don't persist it** (only skip the final
  write step): discarded, since session results would then still flow
  through the core-logic/view-model layers and could easily end up
  being persisted by accident in future changes — the only robust
  guarantee is to interrupt the write path as early as possible.
- **UI-side suppression only** (data keeps being stored, just not
  displayed): explicitly ruled out, see the pitfall in
  `architecture.md` chapter 5.2 and US-6.2.

## Consequences

- `ProgressTrackingGate` is a single, testable place for this behavior
  (ADR-008) — no toggle checks scattered across multiple view models.
- The toggle state itself is always stored (as a setting), even when
  tracking is off — only progress data is affected.
