# ADR-013: Distribution/Signing Strategy per Store

**Status:** Decided
**Date:** Planning phase

## Context

The app must be distributed via the Play Store (Android), App Store
(iOS), and F-Droid/Obtainium (GrapheneOS) (see `docs/architecture.md`,
chapter 7). F-Droid prefers reproducible builds with no proprietary
dependencies.

To clarify: the concrete signing strategy per platform, the
reproducible-build setup for F-Droid, the release process via GitHub
Actions (ADR-003).

## Decision

A dedicated release path per distribution channel, anchored in ADR-003
(GitHub Actions):

- **Play Store**: Android App Bundle, signed with a dedicated release
  keystore (stored as an encrypted GitHub Actions secret). Uploaded
  via the official Google Play Publishing API in a release workflow
  (manually triggered, no auto-publish).
- **F-Droid**: F-Droid builds and signs the app itself from source
  (F-Droid generally does not accept externally built binaries as the
  first signature). Prerequisite: a reproducible Gradle build with no
  proprietary dependencies (Play Billing, FCM, Google Play Services)
  — metadata is submitted to `fdroiddata` once the project is
  release-ready.
- **Obtainium**: an independently signed release APK (a signing key
  separate from the Play Store keystore is possible, but not
  required), attached to a GitHub release produced by the same release
  workflow.
- **App Store**: a signed IPA via an Apple distribution certificate +
  provisioning profile (as GitHub Actions secrets), uploaded via
  Fastlane (`pilot`/`deliver`) to App Store Connect.

## Alternatives

- **Codemagic** (a specialized KMP/mobile CI with built-in signing):
  discarded, since ADR-003 already establishes GitHub Actions as the
  unified CI/CD system for build/test **and** release — a second CI
  system for releases would introduce unnecessary complexity and a
  second place for secrets/configuration.

## Consequences

- A dedicated release workflow is needed (separate from the PR
  build/test workflow), manually triggered (e.g. on a tag push).
- GrapheneOS compatibility (see risks in `architecture.md`) must be
  ensured throughout the Android build — no hard dependency on Google
  Play Services in the app code, regardless of the distribution
  channel.
- Apple signing setup (certificates, profiles) is a separate
  onboarding step and is captured as its own ticket.
