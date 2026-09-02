# ADR-003: CI/CD System

**Status:** Decided
**Date:** Planning phase

## Context

A CI/CD system is needed for linting, tests, and builds across
multiple target platforms (Android, iOS). Initial concerns existed
about possible GitHub Actions costs.

## Decision

**GitHub Actions.** Since the repository is public (open source, MIT),
GitHub Actions on standard runners is free with unlimited minutes —
macOS runners for iOS builds are also free for public repos (possibly
with longer wait times, see risks in `architecture.md`).

## Alternatives

- Other CI systems (e.g. Codemagic for Flutter/KMP + iOS signing) were
  named as a possible alternative but not pursued further, since
  GitHub Actions is sufficient to get started.

## Consequences

- No additional CI costs for standard builds.
- iOS build speed depends on macOS runner availability.
