# Negative Knowledge (Failed Approaches & Discarded Decisions)

> Purpose: prevent already-failed or deliberately discarded approaches
> from being tried again. Before starting a new approach, briefly
> check here whether something similar has already failed once.
>
> Format per entry (free-text log):
> ```
> ## <Date>
> **Tried/considered:** <what was tried or considered as an option>
> **Result:** <why it failed or was discarded>
> **Context/ticket:** <optional reference to a ticket number or ADR>
> ```

## 2026-09-03

**Tried/considered:** Applying the ktlint Gradle plugin once via a
root `subprojects { apply(plugin = libs.plugins.ktlint.get().pluginId) }`
block, so every module (including future ones) gets it automatically
without per-module wiring.

**Result:** Fails with `Extension with name 'libs' does not exist` —
the version-catalog `libs` accessor is not reliably available inside a
`subprojects {}` closure in the root build script, unlike inside each
subproject's own `build.gradle.kts` (where `alias(libs.plugins.x)`
already works for the other plugins). Applied `alias(libs.plugins.ktlint)`
individually in each module's own `plugins {}` block instead (same
pattern already used for `kotlinMultiplatform`/`androidMultiplatformLibrary`/etc.).

**Context/ticket:** #2 (CI pipeline / ktlint setup).

## 2026-09-03

**Tried/considered:** Excluding Compose Multiplatform's generated
resource-accessor Kotlin sources (`build/generated/compose/resourceGenerator/...`)
from ktlint via `KtlintExtension.filter { exclude("**/generated/**") }`
or `exclude { entry -> ... }`, configured centrally through a root
`subprojects { plugins.withId("org.jlleitschuh.gradle.ktlint") { ... } }`
block.

**Result:** Inconsistent — appeared to work for some Kotlin/Native
source sets but not for `commonMain`, and results changed between runs
depending on Gradle's incremental/up-to-date state (stale `build/`
output made it look like the exclude worked when it was really just
cached). Fix: configure the `ktlint { filter { exclude { ... } } }`
block directly inside `ui/build.gradle.kts` itself (not indirected
through root `subprojects {}`), and always verify ktlint config
changes against a clean build (`rm -rf */build build` first) rather
than trusting an incremental run.

**Context/ticket:** #2 (CI pipeline / ktlint setup).

## 2026-09-03

**Tried/considered:** Passing `Set<ArithmeticOperation>` directly as a
`@Serializable` type-safe navigation route argument
(`Route.DifficultySelection(val selectedOperations: Set<ArithmeticOperation>)`).
Compiled fine and passed `ktlintCheck`/unit tests/CI, but crashed the
app at startup: `IllegalArgumentException: Route ... could not find
any NavType for argument selectedOperations of type
kotlin.collections.LinkedHashSet`.

**Result:** androidx.navigation's type-safe routes only have built-in
`NavType` support for primitives, enums, and `List`/`Array` — verified
directly against the `NavTypeConverter`/`RouteSerializer` source in
androidx.navigation-common (`InternalType` has no `Set` case at all,
only `LIST`/`ARRAY`/enum variants). **`Set` is never supported, with
or without a custom `NavType`.** Fixed by keeping `Set<T>` as the
domain/UI-facing type (ViewModel constructors, screen parameters) and
converting to/from `List<T>` only at the two route-crossing points:
where a view model constructs a `Route` (`.toList()`) and where
`MentalmaticsNavHost` reads one back via `backStackEntry.toRoute()`
(`.toSet()`).

This class of bug is invisible to `ktlintCheck`/unit tests/`assembleDebug`
— it only surfaces when the Compose UI actually composes and the
`NavHost` builds its graph at runtime. Caught only by actually running
the app on an emulator and navigating, not by any automated check in
this project's CI.

**Context/ticket:** #12 (free-practice session UI) — will matter again
for #21/#22, whose routes will also need to carry operation selections.

## 2026-09-03

**Tried/considered:** Original design from ticket derivation:
easy/medium/hard named difficulty levels (US-2.2, implemented in #11
as a `Difficulty` enum), inline correct/incorrect feedback after every
task (implemented in #12), system-keyboard text entry for answers
(implemented in #12), and a separate competitive "timed mode" ticket
(#23, solve as many tasks as possible against a clock, distinct from
untimed free practice).

**Result:** Discarded after the user hands-on tested the merged MVP
screens and found the flow "weak": too many full-screen steps for a
trivial amount of setup, a broken rhythm from tapping Check/Next after
every single task, and system-keyboard popup interrupting entry.
Replaced with: digit-count-based difficulty (US-2.2), a single
combined setup screen, a custom on-screen number pad (US-5.3), no
per-task feedback with a report at session end (US-5.2), and a session
length chosen by task count *or* duration (US-5.1) — which subsumes
what ticket #23 would have built, so #23 was closed rather than
implemented separately.

Lesson: none of this was catchable by code review, tests, or CI —
it only became visible once a human actually used the running app.
Ticket-by-ticket ADR-driven planning got the architecture right but
didn't validate the actual interaction design; that needed real usage.

**Context/ticket:** supersedes #11, #12 (as originally scoped) and
#23 (closed, folded into #12's redesign).

## 2026-09-03 (continued)

**Tried/considered:** Adding `org.jetbrains.compose.material:material-icons-extended`
pinned to the same version as the rest of Compose Multiplatform
(`composeMultiplatform = "1.11.1"`), to replace the numeric keypad's
text-glyph backspace/confirm icons ("⌫"/"✓") with real Material icons.

**Result:** Fails dependency resolution —
`Could not find org.jetbrains.compose.material:material-icons-extended:1.11.1`.
This artifact (and its sibling `material-icons-core`, both under the
legacy Material 2 `org.jetbrains.compose.material` group) is **not**
published in lockstep with the main Compose Multiplatform version; it
stops at `1.7.3` (last published 2024-12-19) on Maven Central,
regardless of how new the rest of the CMP stack is. Fixed by pinning
it to its own version (`composeMaterialIcons = "1.7.3"` in
`gradle/libs.versions.toml`) independent of `composeMultiplatform`.
Compiled and ran fine mixed with CMP 1.11.1/material3 1.11.0-alpha07 —
the icon pack is just `ImageVector` data with a small, stable API
surface, so the version skew hasn't caused problems in practice.
Before bumping `composeMultiplatform` again, check
`https://repo1.maven.org/maven2/org/jetbrains/compose/material/material-icons-extended/maven-metadata.xml`
for whether a newer version now exists.

**Context/ticket:** #12 (free-practice session UI) — keypad icon
redesign.

## 2026-09-03 (continued)

**Tried/considered:** Sizing the session screen's top info block
(progress/task/answer/retry-feedback text) with `wrap-content` height
in a plain `Column`, with the numeric keypad placed directly below it.

**Result:** In "Retry until correct" mode, the extra "Not quite, try
again" line only appears after a wrong answer, growing the info
block's height and pushing the entire keypad down — the confirm button
visibly jumps to a new position after every wrong attempt, which is
disorienting for rapid entry. Fixed by giving the info block and the
keypad each a fixed `Modifier.weight(1f)` half of the screen (instead
of content-based sizing), with the keypad bottom-aligned inside its
half. This makes the keypad's position depend only on screen height,
not on which optional child views are currently shown, and as a side
effect puts it in the lower half of the screen for easier one-handed
thumb reach.

**Context/ticket:** #12 (free-practice session UI) — keypad
stability/reachability fix.

## 2026-09-03 (continued)

**Tried/considered:** Sizing each keypad button with
`Modifier.weight(1f).aspectRatio(1f)` inside its `Row` — width comes
from dividing the row's available width three ways, height is then
forced to match that width to keep the button square. This was fine
when the keypad's container height was unconstrained, but after
confining the keypad to the bottom half of the screen (previous
entry), the 4 rows of width-derived square buttons together needed
more height than that half actually had.

**Result:** The container didn't shrink the buttons to fit; the last
row got compressed against the one above it, so the "7/8/9" and
"backspace/0/✓" rows visually overlapped — only reproducible on the
device/emulator, not from reading the layout code. Fixed by measuring
the actual available width *and* height with `BoxWithConstraints` and
computing one explicit `buttonSize = minOf(width-derived size,
height-derived size)` applied via `Modifier.size(...)`, dropping
`weight`/`aspectRatio` entirely. Lesson: `aspectRatio` only guarantees
the *shape* (square) from one known dimension — it does not check
whether the derived other dimension actually fits the container. Any
grid sized by aspect ratio from one axis needs its available space on
the *other* axis checked too (`BoxWithConstraints`, or an explicit
size computed from both dimensions) once that grid is placed somewhere
with a bounded size on both axes.

**Context/ticket:** #12 (free-practice session UI) — keypad overlap
fix (found via user screenshot after the reachability change above).

## 2026-09-04

**Tried/considered:** Backtick-quoted `kotlin.test` function names
containing a comma, e.g. `` fun `task count is complete once enough
tasks are done, regardless of elapsed time`() ``. Compiles and runs
fine on the JVM/Android host test target.

**Result:** Fails only on Kotlin/Native targets —
`compileTestKotlinIosSimulatorArm64` errors with `Name contains
illegal characters: ","`. Kotlin/Native's name-mangling for backtick
identifiers is stricter than the JVM's; commas (and likely other
punctuation used for symbol-name delimiting) aren't allowed, even
though the same identifier is legal Kotlin for JVM targets. This is
invisible to `:core:testAndroidHostTest`/`:ui:testAndroidHostTest`
(this project's only targets normally run without a Mac) and only
surfaces in the CI `iOS build & test` job, or by explicitly running
`compileTestKotlinIosSimulatorArm64` locally (which **can** be
cross-compiled on Linux, unlike actually *running* the iOS simulator
tests). Fixed by rephrasing the test names to avoid commas entirely.
Before writing a new backtick test name, avoid commas (and prefer
plain words/spaces generally) — or run
`./gradlew :core:compileTestKotlinIosSimulatorArm64
:ui:compileTestKotlinIosSimulatorArm64` locally before pushing, since
this doesn't require macOS.

**Context/ticket:** discovered via CI failure on PR #38 (ticket #37's
redesign branch).
