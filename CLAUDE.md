# CLAUDE.md

**Mentalmatics** — mental math training app for adults — self-paced
self-training, three exercise areas (general mental math, everyday
scenarios, computing-related math) plus a Tips & Tricks section.
Details: `docs/vision.md`.

## Tech Stack

- Kotlin Multiplatform / Compose Multiplatform
- Purely local persistence (no server component)
- Target platforms: Android (Play Store), iOS (App Store, iPhone+iPad),
  GrapheneOS (F-Droid, Obtainium)
- MIT license, open source, monorepo
- CI/CD: GitHub Actions

## Important Rules

- **MUST** read `docs/context.md` and `docs/negative-knowledge.md`
  before starting a ticket.
- **MUST** perform a self-review before opening a PR (see
  `docs/git-strategy.md`).
- **MUST** use Conventional Commits (`feat:`, `fix:`, `refactor:`, …).
- **MUST** work on a feature branch:
  `feature/<ticket-id>-short-description`.
- **MUST** check whether an ADR already exists (`docs/adr`) before any
  major architecture decision.

## Where to Find What

| Topic | File |
|---|---|
| Vision/product idea | `docs/vision.md` |
| Current project status | `docs/context.md` |
| Archived older context | `docs/context-archive.md` |
| Failed/discarded approaches | `docs/negative-knowledge.md` |
| Architecture (arc42) | `docs/architecture.md` |
| Architecture decisions | `docs/adr` |
| Functional requirements | `docs/functional-requirements.md` |
| Glossary | `docs/glossary.md` |
| Example tasks | `docs/examples.md` |
| Git workflow | `docs/git-strategy.md` |
| Coding conventions | `docs/coding-conventions.md` |

## Commands

_Placeholder — will be filled in once the project setup (Gradle
structure, see ADR-009) is in place._
