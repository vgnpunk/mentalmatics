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

**US-2.2** As a user, I want to be able to choose the difficulty
level, so that number size/complexity matches my level.

- AC: Multiple difficulty levels selectable (at least easy/medium/
  hard).
- AC: Difficulty affects number size and task complexity.

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

**US-5.1** As a user, I want to be able to choose between timed mode
(timer, solve as many tasks as possible) and free practice (no time
limit), so that I can decide for myself how I want to practice.

- AC: Both modes are available for all three exercise areas.
- AC: Selection happens before the session starts.

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
