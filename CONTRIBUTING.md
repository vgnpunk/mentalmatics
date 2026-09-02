# Contributing

This project is open source (MIT) and accepts contributions from
humans **and** AI agents (e.g. Claude Code). AI agents additionally
follow the rules in [`CLAUDE.md`](CLAUDE.md).

## Workflow

See [`docs/git-strategy.md`](docs/git-strategy.md) for details on
branching (`feature/<ticket-id>-...`), Conventional Commits, and
squash-merging into `main`.

## Working on Tickets

Tickets are derived from the [functional
requirements](docs/functional-requirements.md) and the
[architecture](docs/architecture.md) — only after all relevant
[ADRs](docs/adr/) have been decided. Each ticket includes:

- Title and description
- Acceptance criteria
- Affected files/modules
- Dependencies on other tickets

## Before Opening a Pull Request

Self-review checklist:

- [ ] All acceptance criteria of the ticket met?
- [ ] Tests added where sensible (see [Coding
      Conventions](docs/coding-conventions.md))?
- [ ] Linting clean (also checked by CI)?
- [ ] [`docs/negative-knowledge.md`](docs/negative-knowledge.md)
      checked to avoid known dead ends?

## Code Style

See [`docs/coding-conventions.md`](docs/coding-conventions.md).

## Questions/Discussion

Please post open questions about a ticket directly as a comment on the
respective GitHub issue, so the context is preserved.
