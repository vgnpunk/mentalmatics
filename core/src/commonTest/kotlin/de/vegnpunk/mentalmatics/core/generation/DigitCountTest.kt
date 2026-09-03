package de.vegnpunk.mentalmatics.core.generation

import kotlin.test.Test
import kotlin.test.assertFailsWith

class DigitCountTest {
    @Test
    fun `accepts every digit count from 1 to the maximum`() {
        (1..DigitCount.MAX_DIGITS).forEach { DigitCount(it) }
    }

    @Test
    fun `rejects zero or fewer digits`() {
        assertFailsWith<IllegalArgumentException> { DigitCount(0) }
    }

    @Test
    fun `rejects more than the maximum digits`() {
        assertFailsWith<IllegalArgumentException> { DigitCount(DigitCount.MAX_DIGITS + 1) }
    }
}
