package de.vegnpunk.mentalmatics.core.arithmetic

import de.vegnpunk.mentalmatics.core.generation.DigitCount
import kotlin.test.Test
import kotlin.test.assertEquals

class ArithmeticDigitCountTest {
    @Test
    fun `one digit maps to zero through nine`() {
        assertEquals(0..9, DigitCount(1).toArithmeticDifficultyConfig().numberRange)
    }

    @Test
    fun `two digits maps to ten through ninety-nine`() {
        assertEquals(10..99, DigitCount(2).toArithmeticDifficultyConfig().numberRange)
    }

    @Test
    fun `six digits maps to the expected six-digit range`() {
        assertEquals(100_000..999_999, DigitCount(6).toArithmeticDifficultyConfig().numberRange)
    }

    @Test
    fun `always maps to a two-operand config`() {
        assertEquals(2, DigitCount(3).toArithmeticDifficultyConfig().operandCount)
    }
}
