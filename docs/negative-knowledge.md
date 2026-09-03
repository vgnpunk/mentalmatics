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
