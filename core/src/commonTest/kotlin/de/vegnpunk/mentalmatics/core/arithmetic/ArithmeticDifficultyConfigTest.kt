package de.vegnpunk.mentalmatics.core.arithmetic

import de.vegnpunk.mentalmatics.core.generation.Difficulty
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ArithmeticDifficultyConfigTest {
    @Test
    fun `each difficulty maps to a two-operand config`() {
        Difficulty.entries.forEach { difficulty ->
            assertEquals(2, difficulty.toArithmeticDifficultyConfig().operandCount)
        }
    }

    @Test
    fun `harder difficulties use a wider number range`() {
        val easy = Difficulty.EASY.toArithmeticDifficultyConfig().numberRange
        val medium = Difficulty.MEDIUM.toArithmeticDifficultyConfig().numberRange
        val hard = Difficulty.HARD.toArithmeticDifficultyConfig().numberRange

        assertTrue(easy.last < medium.last)
        assertTrue(medium.last < hard.last)
    }
}
