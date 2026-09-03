package de.vegnpunk.mentalmatics.core.arithmetic

import de.vegnpunk.mentalmatics.core.generation.Difficulty
import de.vegnpunk.mentalmatics.core.generation.DifficultyConfig

/** Maps a named [Difficulty] to the number range used for arithmetic tasks (US-2.2). */
fun Difficulty.toArithmeticDifficultyConfig(): DifficultyConfig =
    when (this) {
        Difficulty.EASY -> DifficultyConfig(numberRange = 1..20, operandCount = 2)
        Difficulty.MEDIUM -> DifficultyConfig(numberRange = 1..100, operandCount = 2)
        Difficulty.HARD -> DifficultyConfig(numberRange = 1..1000, operandCount = 2)
    }
