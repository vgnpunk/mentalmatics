package de.vegnpunk.mentalmatics.core.generation

import kotlinx.serialization.Serializable

/**
 * Named difficulty levels (US-2.2). Exercise areas map each level to
 * their own [DifficultyConfig] (see e.g. `core.arithmetic`), since
 * what counts as "hard" differs per area.
 */
@Serializable
enum class Difficulty { EASY, MEDIUM, HARD }
