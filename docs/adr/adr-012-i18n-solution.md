# ADR-012: i18n Solution

**Status:** Decided
**Date:** Planning phase

## Context

The app supports German and English in the MVP (see `docs/vision.md`),
with further languages planned for later. A technical solution for
Kotlin Multiplatform is needed that models this cleanly and
structurally from the start (not retrofitted).

To clarify: e.g. Compose Resources vs. moko-resources vs. lyricist.

## Decision

**Compose Multiplatform Resources** (`compose.components.resources`,
the official JetBrains mechanism for localized string resources). No
additional third-party dependency needed, direct integration into the
Gradle/Compose Multiplatform build (ADR-001, ADR-003).

## Alternatives

- **moko-resources**: an established community solution, but with the
  official Compose Resources maturing, it offers no benefit over the
  official, better-integrated option.
- **lyricist**: a lean, Compose-native API, but without the built-in
  resource bundling (images, strings) of the official solution — would
  have been sufficient for strings alone, but was discarded in favor
  of a single, official solution for all resource types.

## Consequences

- All user-facing strings live from the start in `strings.xml`-style
  resource files (`de`, `en`), never as hardcoded text in code — also
  in the `:content` module (Tips & Tricks).
- Further languages can be added later by adding more resource
  folders, with no code changes (see vision: multilingual support
  planned for from the start).
