# Architecture (arc42)

## 1. Introduction and Goals

The app is a cross-platform mental math training tool for adults.
See `docs/vision.md` for details on target audience and core value.

**Quality goals (prioritized):**

1. **Privacy**: all user data stays local on the device.
2. **Platform openness**: runs on Android (Play Store), GrapheneOS
   (F-Droid/Obtainium), and iOS (App Store), without forcing
   proprietary Google services.
3. **Maintainability**: clear module separation, so that AI-assisted
   ticket work can make isolated changes without endangering other
   areas.
4. **Simplicity**: no server, no unnecessary complexity — the app is
   deliberately kept functionally lean (see non-goals in vision.md).

## 2. Constraints

- No server/backend — purely local data storage.
- MIT license, open source, monorepo.
- Must work on GrapheneOS without Google Play Services and be
  distributable via F-Droid/Obtainium (preferred: no proprietary
  dependencies, reproducible builds).
- Must run on iPhone and iPad (App Store) as well as Android (Play
  Store).
- Budget/team: a single person + AI agent (Claude Code) as the main
  developer, a human reviews every PR.
- CI/CD: GitHub Actions (free for public repos).

## 3. System Scope and Context

**Business context:**

The app offers three exercise areas (general mental math, real-life
everyday scenarios, computing-related math) plus an informational
Tips & Tricks section. Users configure practice sessions (operation
types, difficulty, timed on/off) and optionally see their progress.

**Technical context:**

The app has no external systems/interfaces — no server API, no cloud
sync, no third-party services. The only external touchpoints are the
respective OS APIs (storage, theming/dark mode, language setting) and
the distribution channels (Play Store, App Store, F-Droid).

## 4. Solution Strategy

- **Kotlin Multiplatform** with **Compose Multiplatform** for the UI,
  to serve Android, iOS, and potentially desktop from a single
  codebase (ADR-001).
- Purely local persistence without a server component (ADR-004).
- Modular structure that separates core logic (task generation,
  progress) from UI and platform-specific code (details: ADR-009).
- Architecture and detail decisions are made **entirely before**
  ticket derivation (see `docs/context.md`), to avoid later tickets
  invalidating earlier decisions.

## 5. Building Block View

> Note: The exact module split is finally decided in ADR-009
> (layer-based split, Gradle modules). The following outline
> corresponds to the modules defined there.

### 5.1 Core Modules (Whitebox Overview)

- **Core logic** — task generation (all three exercise areas),
  difficulty logic, number base conversion, platform-independent.
- **Persistence** — local storage of settings and optional progress.
- **UI (Compose Multiplatform)** — shared UI components for all
  platforms.
- **Platform shells** — thin platform-specific entry points (Android
  app, iOS app) that wire in the shared logic/UI.
- **Content (Tips & Tricks)** — static/structured content for the
  informational section.

### 5.2 Pitfalls per Building Block

- **Core logic**: random generation must guarantee real variance (no
  repetitions in quick succession); custom-base conversion must
  cleanly validate edge cases (base 2–36 or similar).
- **Persistence**: the progress-tracking toggle must be implemented so
  that when "off", truly no data is produced (not just suppressed in
  the UI) — see ADR-015.
- **UI**: Compose Multiplatform is comparatively young for iOS — test
  platform quirks (e.g. safe area on iPad) early.
- **Platform shells**: GrapheneOS compatibility requires avoiding
  dependencies on Google Play Services (e.g. Play Billing, FCM), or
  making them cleanly optional.
- **Content**: multilingual support (DE/EN) must be structurally
  planned for from the start, not embedded in free text after the
  fact.

## 6. Runtime View

Example flows (to be extended with sequence diagrams as needed):

- **Starting a practice session**: user selects exercise area →
  operation types → difficulty → mode (timed/free) → core logic
  generates tasks → UI shows tasks one after another → on completion:
  optionally save the result to persistence (if tracking is active).
- **Number base exercise**: user selects source/target system
  (including custom base) → core logic generates conversion tasks.
- **Reading Tips & Tricks**: user opens the section → content module
  delivers structured, localized content → display only, no
  persistence.

## 7. Deployment View

- **Android**: Play Store (signed APK/AAB).
- **GrapheneOS**: F-Droid (reproducible build, no proprietary
  dependencies) and Obtainium (direct APK download from, e.g., GitHub
  releases).
- **iOS**: App Store (iPhone + iPad, signed IPA via Apple certificate).
- **CI/CD**: GitHub Actions builds and tests all target platforms;
  signing details are defined in ADR-013.

## 8. Cross-Cutting Concepts

- **i18n**: German + English in the MVP, structure extensible
  (ADR-012).
- **Theming**: light/dark, follows the system setting.
- **Accessibility**: not an MVP blocker, but components are built from
  the start with a screen-reader-/contrast-capable structure, to avoid
  having to retrofit it later.
- **Error handling**: purely local app with no network error cases;
  focus is on robust input validation (e.g. custom base).
- **Test concept**: see ADR-008.

## 9. Architecture Decisions

See `docs/adr/`. This file does not duplicate ADR content, only
references it.

## 10. Quality Requirements

Brief summary (details, if any, in
`docs/functional-requirements.md`):

- Offline capability: complete (no network dependency).
- Privacy: no telemetry, no cloud transmission.
- Platform support: Android, iOS (iPhone+iPad), GrapheneOS.
- Performance: task generation and UI response with no noticeable
  delay (no hard numeric target defined for the MVP).

## 11. Risks and Technical Debt

- **iOS CI capacity**: macOS runners on GitHub Actions are free but
  may involve wait times — a risk for build speed.
- **GrapheneOS compatibility**: must be actively tested, since Compose
  Multiplatform is primarily developed for standard Android/iOS.
- **Compose Multiplatform iOS maturity**: a younger target framework
  for iOS compared to native alternatives — potential gaps in
  platform-specific APIs.
- **Missing adaptive difficulty (deliberately deferred)**: could be
  added in a later version, but is not a v1 risk.

## 12. Glossary

See `docs/glossary.md`.
