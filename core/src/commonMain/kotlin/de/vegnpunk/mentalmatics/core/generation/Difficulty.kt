package de.vegnpunk.mentalmatics.core.generation

/**
 * Named difficulty levels (US-2.2). Exercise areas map each level to
 * their own [DifficultyConfig] (see e.g. `core.arithmetic`), since
 * what counts as "hard" differs per area.
 */
enum class Difficulty { EASY, MEDIUM, HARD }
