# ADR-005: Architecture Documentation Standard

**Status:** Decided
**Date:** Planning phase

## Context

AI-assisted development needs a consistent, well-known structure for
architecture documentation, so both humans and AI agents can orient
themselves quickly.

## Decision

The **arc42 template** is used for `docs/architecture.md` (12 standard
chapters: introduction/goals, constraints, system scope and context,
solution strategy, building block view, runtime view, deployment view,
cross-cutting concepts, architecture decisions, quality requirements,
risks/technical debt, glossary).

## Alternatives

- **C4 model**: discarded in favor of arc42, which was judged to be
  the more widespread standard in this project's context.

## Consequences

- Architecture decisions themselves are not duplicated in
  `architecture.md`, but referenced out to `docs/adr/` (chapter 9).
- The glossary is not duplicated, but referenced out to
  `docs/glossary.md` (chapter 12).
