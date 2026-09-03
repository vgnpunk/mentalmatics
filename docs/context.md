# Context (Onboarding Snapshot for AI Agents)

> This file is a **living document**. It records what's currently
> relevant, so an AI agent (e.g. Claude Code) can quickly understand
> the current state at the start of a ticket. Content that's no longer
> actively needed moves to `context-archive.md` — it is **not
> deleted**, only archived.

## Current Project Status

- Phase: **MVP implementation started**. Ticket #1 (Gradle module
  structure per ADR-009) is done: `:core`, `:data`, `:ui`, `:content`
  Gradle modules exist alongside `:androidApp`/`:iosApp`, replacing the
  wizard-generated `:shared` module. `:ui` holds a minimal placeholder
  `App()` (no product UI yet) and produces the iOS framework
  (`Ui.framework`, embedded via `:ui:embedAndSignAppleFrameworkForXcode`).
  `:core`/`:data`/`:content` are empty skeletons, populated by later
  tickets.
- Ticket #2 (CI pipeline per ADR-003) is done: `.github/workflows/ci.yml`
  runs on every PR/push to `main` — `ktlintCheck` (ubuntu), Android
  build+test (ubuntu), iOS simulator tests + Xcode build (macos).
  ktlint (org.jlleitschuh.gradle.ktlint) is wired into every module's
  own `build.gradle.kts` (not a root `subprojects {}` block — the
  version-catalog `libs` accessor isn't reliably available there); a
  root `.editorconfig` disables `chain-method-continuation` for
  `*.gradle.kts` (fights idiomatic version-catalog chains) and allows
  PascalCase for `@Composable`-annotated functions.
- Decided ADRs: ADR-001 through ADR-015 (see `docs/adr/`) — all ADRs
  are decided.

## Active/Open Decisions

No ADRs remain open. New architecture decisions that become necessary
during implementation get a new ADR number (starting at ADR-016)
following the same format.

## Tech Stack in Detail (from ADR-006 to ADR-015)

- Persistence: SQLDelight (ADR-006).
- DI: Koin (ADR-007).
- Tests: kotlin.test + kotlinx-coroutines-test, manual fakes instead
  of a mocking framework (ADR-008).
- Modules: layer-based split — `:core`, `:data`, `:ui`, `:content`,
  `:androidApp`, `:iosApp` (ADR-009).
- State management: StateFlow view models, unidirectional data flow,
  no external MVI framework (ADR-010).
- Navigation: the official androidx.navigation Compose Multiplatform
  library (ADR-011).
- i18n: Compose Multiplatform Resources (ADR-012).
- Distribution: Play Store (keystore secret), F-Droid (built by
  F-Droid itself), Obtainium (signed APK via GitHub release), App
  Store (Fastlane) — details in ADR-013.
- Task generation: a `TaskGenerator` interface per exercise area, a
  declarative `DifficultyConfig`, a ring buffer against repetitions
  (ADR-014).
- Tracking toggle: a hard gate on the write path in `:core`, not just
  in the UI (ADR-015).

## Currently Relevant Constraints (Summary)

- Purely local data storage, no server, no accounts.
- Must run on Android (Play Store), GrapheneOS (F-Droid/Obtainium),
  and iOS (App Store, iPhone+iPad) — no dependency on Google Play
  Services.
- Kotlin Multiplatform / Compose Multiplatform as the tech stack.
- MIT license, open source, monorepo.
- No telemetry, no ads.

See `docs/vision.md` and `docs/architecture.md` for details.

## MVP Scope (GitHub Milestone "MVP")

To get to a testable version faster, the backlog is split into two
milestones:

- **MVP**: project setup, `TaskGenerator` foundation, **only** the
  "general mental math" exercise area (addition, subtraction,
  multiplication, division), **free practice only** (no timed mode),
  progress tracking (storage + toggle + display, without an explicit
  delete function), the Tips & Tricks section, DE/EN language
  switching.
- **Post-MVP**: the everyday-scenarios and number-base exercise areas,
  timed mode, the "delete progress data" action, store distribution
  (Play Store, App Store, Obtainium, F-Droid).

Note: `docs/vision.md` still describes all three exercise areas as the
app's core value/USP — the MVP prioritization is a delivery order, not
a change to the vision.

## Current Focus

MVP tickets (milestone "MVP") exist as GitHub issues in
`vgnpunk/mentalmatics`. Tickets #1 (module structure) and #2 (CI
pipeline) are merged into `main`. Remaining MVP tickets (#3–#20) are
ready for assignment (see `docs/git-strategy.md` — tickets are
assigned manually, the AI does not pick tickets on its own beyond
what's been explicitly assigned).

## Important References

- `docs/negative-knowledge.md` — check before trying a new approach,
  whether something similar has already failed/been discarded.
- `docs/context-archive.md` — archived, older context.
- `CLAUDE.md` (repo root) — entry point and mandatory rules for the
  AI.
