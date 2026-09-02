# ADR-008: Testing Strategy/Framework

**Status:** Decided
**Date:** Planning phase

## Context

Per `docs/git-strategy.md`, tests should be part of each ticket
contextually (not rigidly mandatory). A fundamental testing strategy
is needed (which test types, which framework), so that
`docs/coding-conventions.md` can be filled in accordingly.

To clarify: e.g. kotlin.test vs. Kotest, mocking approach, scope of
unit vs. integration tests.

## Decision

**kotlin.test** (with `kotlinx-coroutines-test` for coroutines/flows)
as the single test framework, since it officially runs on all KMP
targets (including iOS/Native) without restrictions. Instead of a
mocking framework, **manual fakes/test doubles** are used (e.g.
in-memory implementations of the repository interfaces), since common
JVM mocking libraries (e.g. MockK) are not fully supported on
Kotlin/Native.

**Test focus (prioritized):**

1. **Core logic** (task generation, difficulty logic, number base
   conversion, see ADR-014): high coverage, since it's
   platform-independent, pure logic with the highest risk of bugs.
2. **Persistence**: integration tests against the in-memory SQLDelight
   driver (especially tracking-toggle behavior, see ADR-015).
3. **UI**: not an MVP blocker; Compose UI tests are added case by
   case, when a component contains complex state logic.

## Alternatives

- **Kotest**: more expressive assertions/property-based testing, but
  historically incomplete support on Kotlin/Native targets — discarded
  in favor of the official, guaranteed cross-platform solution.

## Consequences

- `docs/coding-conventions.md` gets a concrete testing-conventions
  chapter (naming scheme, location of tests per module).
- Fake implementations of the repository interfaces are maintained
  under the `test` source set of the respective module and wired into
  tests via Koin (ADR-007).
