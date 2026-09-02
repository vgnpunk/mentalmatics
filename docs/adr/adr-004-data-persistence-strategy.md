# ADR-004: Data Persistence Strategy

**Status:** Decided
**Date:** Planning phase

## Context

The app deliberately forgoes accounts, cloud sync, and server
components (see `docs/vision.md`, design guardrails: privacy-first).

## Decision

**Purely local data storage.** There is no server component. All
settings and optional progress data are stored exclusively locally on
the device.

## Alternatives

- Local + later an optional self-hosted sync server — discarded for
  v1, could be re-evaluated for a later version.

## Consequences

- No data-loss risk from server outages, but also no cross-device sync
  in v1.
- Significantly reduces attack surface and privacy overhead.
- The concrete local DB library is decided in ADR-006.
