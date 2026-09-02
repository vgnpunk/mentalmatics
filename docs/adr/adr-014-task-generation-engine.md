# ADR-014: Task Generation Engine

**Status:** Decided
**Date:** Planning phase

## Context

All three exercise areas (see `docs/functional-requirements.md`) need
an engine that generates tasks randomly but sensibly, based on the
selected operation types/difficulty levels — including custom-base
conversion with validation.

To clarify: the concrete algorithm/architecture of the generation
engine, avoidance of repetitions, difficulty parametrization.

## Decision

**A shared `TaskGenerator<T>` interface per exercise area** (strategy
pattern), parametrized by a declarative `DifficultyConfig` (number
range, operand count, allowed operation types) instead of hardcoded
branching per difficulty level:

- **General mental math**: `ArithmeticTaskGenerator`, controlled by
  `OperationSelection` (multi-select, US-1.1/US-2.1) and
  `DifficultyConfig` (number range/complexity, US-2.2).
- **Everyday scenarios**: `ScenarioTaskGenerator` per scenario type
  (unit-price comparison, discount, tipping), text templates via the
  i18n solution (ADR-012), numeric values via the same
  `DifficultyConfig` structure.
- **Number base systems**: `BaseConversionTaskGenerator`,
  source/target base independently selectable (US-4.3), custom base
  validated against the range **2–36** (standard alphanumeric alphabet
  0–9/A–Z, see US-4.2), invalid-base errors surfaced as an explicit
  validation result (no crash/exception in the UI path).

**Repetition avoidance**: a per-session ring buffer of the last N
generated tasks (raw values, not just the result); a new candidate is
discarded and redrawn if it's already in the buffer.
`kotlin.random.Random` is seeded per session, so generation is
deterministic and testable (ADR-008).

## Alternatives

- **A single monolithic generator with a large `when` block across all
  exercise areas**: discarded — contradicts the module goal from
  ADR-009 (clear separation, no god class) and makes adding new
  scenario types harder.

## Consequences

- New difficulty levels or scenario types require no change to the
  core engine, only a new `DifficultyConfig` or scenario definition
  (data-driven).
- The entire engine lives in `:core` (ADR-009), is pure Kotlin with no
  platform/UI dependency, and is therefore fully unit-testable.
