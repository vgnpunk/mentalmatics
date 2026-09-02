# ADR-001: Cross-Platform Framework

**Status:** Decided
**Date:** Planning phase

## Context

The app must support Android, iOS (iPhone + iPad), and GrapheneOS.
Separate native codebases would be unnecessarily costly to maintain
for a single-person project with AI-assisted development. The main
options considered were Flutter (Dart) and Compose Multiplatform
(Kotlin).

## Decision

**Compose Multiplatform** (Kotlin) will be used, aiming for native
performance and a shared codebase for Android, iOS, and potentially
desktop.

## Alternatives

- **Flutter**: large ecosystem, good F-Droid experience in the
  community — discarded in favor of Kotlin/Compose, among other
  reasons due to the preferred language and native performance goals.

## Consequences

- iOS support via Compose Multiplatform is comparatively young —
  platform quirks must be tested early (see risks in
  `architecture.md`).
- The Kotlin ecosystem (libraries for DB, DI, testing) must be
  suitable for all target platforms — relevant for ADR-006 through
  ADR-011.
