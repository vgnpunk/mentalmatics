# ADR-007: Dependency Injection Approach

**Status:** Decided
**Date:** Planning phase

## Context

The module structure (see ADR-009) needs a DI approach that is
compatible with Kotlin Multiplatform.

To clarify: e.g. Koin vs. kotlin-inject vs. manual DI.

## Decision

**Koin.** Runtime DI without code generation, full KMP support
(including iOS), low boilerplate, wide adoption, and good
documentation — easier to follow for AI-assisted solo development than
compile-time DI with annotation processing.

## Alternatives

- **kotlin-inject**: compile-time safe (KSP-based), but a more complex
  setup and a smaller ecosystem; the extra compile-time-safety benefit
  doesn't justify the added effort given this project's small number
  of modules.
- **Manual DI**: unwieldy and error-prone without framework support as
  the number of modules grows (core logic, persistence, UI, content,
  platform shells) — discarded.

## Consequences

- Each module from ADR-009 defines its own Koin module; the platform
  shells (Android/iOS) start Koin at app launch.
- Tests can selectively override Koin modules with fake implementations
  (relevant for ADR-008).
