# Coding Conventions

## Language & Formatting

- Kotlin, following the official [Kotlin style
  guide](https://kotlinlang.org/docs/coding-conventions.html).
- Automatic linting/formatting via ktlint, enforced in CI.

## Naming Conventions

- Classes/objects: `PascalCase`
- Functions/variables: `camelCase`
- Files: named after the main class they contain

## Code Organization

Layer-based split across Gradle modules (see ADR-009):

- `:core` — task generation, difficulty logic, number base conversion.
  Pure Kotlin, no Compose/platform dependency. Package split per
  exercise area (`core.arithmetic`, `core.scenarios`,
  `core.baseconversion`).
- `:data` — SQLDelight schemas, repositories for settings and
  progress.
- `:ui` — shared Compose Multiplatform screens, components,
  navigation, view models.
- `:content` — static, localized Tips & Tricks content.
- `:androidApp`, `:iosApp` — thin platform shells.

## Documentation in Code

- KDoc for public APIs/functions.
- Comments only where the code isn't self-explanatory.

## Testing Conventions

See ADR-008 for the underlying decision.

- Framework: `kotlin.test` + `kotlinx-coroutines-test`, no mocking
  library — manual fakes/test doubles for repository interfaces
  instead.
- Tests live in the `test` source set of the respective module, named
  after the class under test (`ArithmeticTaskGeneratorTest` for
  `ArithmeticTaskGenerator`).
- Priority: `:core` (high coverage, pure logic), `:data` (integration
  tests against the in-memory SQLDelight driver, especially
  tracking-toggle behavior), `:ui` (case by case, not an MVP blocker).

## Patterns to Avoid

- The `!!` operator only with an explicit justification in a comment.
- No "god classes" — keep responsibilities clearly separated.
- No deeply nested control logic (prefer early returns).

## Enforcement

Linting/formatting is automatically checked via a CI gate (GitHub
Actions). A PR is not merged on violation.
