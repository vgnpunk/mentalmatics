# ADR-006: Local Database Library

**Status:** Decided
**Date:** Planning phase

## Context

Local persistence of settings and optional progress (ADR-004) needs a
concrete library that works with Kotlin Multiplatform (ADR-001) on
Android and iOS.

To clarify: e.g. SQLDelight vs. Room KMP vs. other options.

## Decision

**SQLDelight.** A single library for both settings and optional
progress, to have exactly one persistence mechanism in the project
(simplicity goal, see `architecture.md` chapter 1). SQL schemas
generate type-safe Kotlin code; drivers exist for Android (Android
SQLite Driver) and iOS (Native SQLite Driver) with no proprietary
dependencies — F-Droid/GrapheneOS-compatible.

## Alternatives

- **Room KMP**: officially multiplatform-capable since Room 2.7, but
  younger in the KMP context and more tightly bound to the
  Android/Jetpack ecosystem; no clear advantage over SQLDelight for
  this project.
- **multiplatform-settings + a separate solution for progress**: would
  introduce two persistence mechanisms (key-value + relational) with
  no real benefit given the small data volume here — discarded in
  favor of a single mechanism.

## Consequences

- Settings (language, theme override, timer/tracking toggle) and
  progress data (sessions, results) are modeled as SQLDelight tables
  in the `:data` module (see ADR-009).
- Schema migrations must be planned for from the start (SQLDelight
  supports versioned `.sqm` migration files).
- Relevant for ADR-015 (tracking toggle): the write path for progress
  tables must be cleanly gated by the toggle.
