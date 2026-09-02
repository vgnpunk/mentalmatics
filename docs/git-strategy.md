# Git Strategy

## Branching Model

One feature branch per ticket/feature, branched off from `main`. No
direct work on `main`.

## Branch Naming Convention

```
feature/<ticket-id>-short-description
```

Example: `feature/42-timed-mode`

## Commit Convention

[Conventional Commits](https://www.conventionalcommits.org/):

- `feat:` — new feature
- `fix:` — bug fix
- `refactor:` — code restructuring without behavior change
- `test:` — adding/changing tests
- `docs:` — documentation change
- `chore:` — other maintenance work (dependencies, build config, etc.)

## Merge Strategy

**Squash and merge** into `main` — one clean commit per feature in the
history. The feature branch is deleted after merging.

## Review Process

A human reviews **every** pull request before merging. No automatic
merging, even with a green CI.

## Ticket Assignment

Tickets are assigned manually. The AI does not select tickets on its
own, but works through them one by one, as assigned.

## Self-Review Before a Pull Request

Before opening a pull request, the AI checks itself:

- Are all acceptance criteria of the ticket met?
- Were meaningful tests added (see testing conventions)?
- Is the diff clean and scoped to the ticket?
- Was `docs/negative-knowledge.md` checked for known dead ends?

See also the PR template (`.github/PULL_REQUEST_TEMPLATE.md`).

## Tests as Part of the Commit

Tests are not rigidly mandatory for every ticket — the AI decides
contextually whether and which tests make sense for the respective
change (see ADR-008 for the overall testing strategy).

## Handling Ticket Dependencies

The order and dependencies between tickets are clarified **up front**
during the architecture/planning phase (see `docs/architecture.md` and
`docs/adr/`) — not discovered only during implementation. Goal: a
ticket should not be invalidated by a later ticket.
