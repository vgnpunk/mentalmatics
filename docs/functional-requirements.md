# Functional Requirements

> Format: user stories ("As a user, I want [X], so that [Y]"), grouped
> by topic. Acceptance criteria are kept coarse — detailed elaboration
> (at the ticket level) happens only after the architecture/ADR
> planning phase is complete (see `docs/context.md`).

## 1. Task Selection (cross-cutting)

**US-1.1** As a user, I want to be able to selectively enable/disable
individual operation types (e.g. addition only, subtraction only, any
mix), so that I can practice exactly what I need.

- AC: Selection is possible granularly per operation type.
- AC: Multi-selection/combination is possible.
- AC: Selection applies consistently across all three exercise areas,
  wherever it makes functional sense.

## 2. General Mental Math

**US-2.1** As a user, I want to be able to choose between addition,
subtraction, multiplication, and division, so that I can specifically
train individual arithmetic operations.

**US-2.2** As a user, I want to be able to choose the number of digits
the numbers involved have, so that task complexity matches my level.

- AC: Digit count is selectable (e.g. 1 to 6 digits).
- AC: Digit count directly determines the number range operands are
  drawn from (e.g. 2 digits → 10–99).

> Revision note: originally specified as named difficulty levels
> (easy/medium/hard). Changed to direct digit-count selection after
> hands-on MVP testing — named levels hid what was actually being
> controlled (number size) behind an extra layer of indirection.

## 3. Real-Life Everyday Scenarios

**US-3.1** As a user, I want to solve tasks based on realistic
everyday situations (e.g. unit-price comparison, discount calculation,
tipping), so that I can apply what I've learned in real life.

- AC: At least the scenario types unit-price comparison, discount, and
  tipping are present in the MVP.
- AC: Tasks are phrased in everyday language, not as abstract math
  problems.

## 4. Computing-Related Math

**US-4.1** As a user, I want to practice converting between binary,
octal, and hexadecimal via quick selection, so that I can quickly
train the most common number systems.

**US-4.2** As a user, I want to additionally use a freely configurable
custom base for conversion, so that I can also practice unusual number
systems.

**US-4.3** As a user, I want to freely choose which system is
converted from and to (including custom base on both sides), so that I
can practice exactly the direction I need.

- AC: Source and target system are independently selectable.
- AC: Custom base is validated (sensible value range).

## 5. Practice Modes

**US-5.1** As a user, I want to choose how long a practice session
lasts — either a fixed number of tasks or a fixed duration — so that I
can fit practice into the time I have.

- AC: Session length is selectable by task count or by duration.
- AC: Selection happens before the session starts.
- AC: Both modes are available for all three exercise areas.

> Revision note: originally specified as a choice between a
> competitive "timed mode" (separate from free practice, solve as many
> as possible) and untimed "free practice". Merged into a single
> concept after hands-on MVP testing: every session is "free practice"
> in spirit (no per-task pressure, see US-5.2), just with a
> user-chosen stopping point instead of running forever. A genuinely
> competitive/scored timed mode (e.g. with a leaderboard) may still be
> considered later as a separate feature (see `docs/negative-knowledge.md`
> for the discarded original ticket).

**US-5.2** As a user, I want to solve tasks back-to-back without
interruption during a session, and see a summary of my results only at
the end, so that my practice rhythm isn't broken by feedback after
every single task.

- AC: No correct/incorrect indication is shown during the session.
- AC: A report is shown at the end of the session listing the score
  and every incorrect answer together with the correct solution.

**US-5.3** As a user, I want to enter my answer using an on-screen
number pad instead of the system keyboard, so that entry is fast and
the screen doesn't get covered by the system keyboard popping up.

- AC: Digits 0–9, a backspace action, and a confirm action are
  available on-screen.
- AC: The system keyboard never appears for answer entry.

## 6. Progress Tracking

**US-6.1** As a user, I want to see my practice progress recorded
locally, so that I can track my development.

**US-6.2** As a user, I want to be able to fully turn off progress
tracking, so that no data is recorded at all if I don't want that.

- AC: Toggle is easily accessible (e.g. in settings).
- AC: When "off", no progress data is generated or stored (see
  ADR-015).

## 7. Tips & Tricks Section

**US-7.1** As a user, I want to be able to read a section with
calculation techniques (e.g. the Trachtenberg system) and explanations
(e.g. how to convert between number systems), so that I gain
additional knowledge about mental math.

- AC: The section is purely informational (text/examples), no
  interactive quizzing.
- AC: Initially contains a small amount of but complete content
  (MVP quality, no placeholder text).

## 8. Settings

**US-8.1** As a user, I want to be able to switch the app language
between German and English, so that I can use it in my preferred
language.

**US-8.2** As a user, I want the app to automatically follow my
system's light/dark mode, so that it fits in with my device.

**US-8.3** As a user, I want the app's color scheme to match my
device's system color/wallpaper where supported, so the app feels
visually integrated with my device.

- AC: On Android 12+, the app uses dynamic (Material You) color
  derived from the system wallpaper.
- AC: On platforms/versions without OS-level dynamic color support
  (iOS, Android below 12), a sensible static color scheme is used
  instead.

## 9. Non-Functional Requirements

- **Offline capability**: The app works fully without an internet
  connection.
- **Privacy**: No telemetry, no analytics, no ads, no cloud
  transmission of data.
- **Platform support**: Android (Play Store), iOS (iPhone + iPad, App
  Store), GrapheneOS (F-Droid, Obtainium).
- **Accessibility**: Not an MVP blocker, but UI components are built
  from the start with screen reader/contrast compatibility in mind.
- **Cost**: Free, no ads, no paywalls.
