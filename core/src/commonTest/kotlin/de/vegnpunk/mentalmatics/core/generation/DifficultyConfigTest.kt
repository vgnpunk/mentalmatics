package de.vegnpunk.mentalmatics.core.generation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DifficultyConfigTest {
    @Test
    fun `stores the configured range and operand count`() {
        val config = DifficultyConfig(numberRange = 1..10, operandCount = 3)

        assertEquals(1..10, config.numberRange)
        assertEquals(3, config.operandCount)
    }

    @Test
    fun `rejects fewer than two operands`() {
        assertFailsWith<IllegalArgumentException> {
            DifficultyConfig(numberRange = 1..10, operandCount = 1)
        }
    }

    @Test
    fun `rejects an empty number range`() {
        assertFailsWith<IllegalArgumentException> {
            DifficultyConfig(numberRange = 10..1, operandCount = 2)
        }
    }
}
