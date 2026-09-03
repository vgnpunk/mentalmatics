package de.vegnpunk.mentalmatics.core.generation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/** Returns candidates from a fixed queue, so tests control the exact draw sequence. */
private class QueueTaskGenerator(
    private val candidates: MutableList<Int>,
    repetitionWindow: Int,
) : AbstractTaskGenerator<Int>(repetitionWindow) {
    override fun createCandidate(): Int = candidates.removeFirst()
}

class AbstractTaskGeneratorTest {
    @Test
    fun `redraws a candidate still within the repetition window`() {
        // window = 2; draws 1,1,2,1,3 -> generate() skips the repeats and returns 1,2,3
        val generator = QueueTaskGenerator(mutableListOf(1, 1, 2, 1, 3), repetitionWindow = 2)

        assertEquals(1, generator.generate())
        assertEquals(2, generator.generate())
        assertEquals(3, generator.generate())
    }

    @Test
    fun `forgets tasks once they fall outside the repetition window`() {
        // window = 1: after drawing 2, "1" is no longer remembered and may repeat.
        val generator = QueueTaskGenerator(mutableListOf(1, 2, 1), repetitionWindow = 1)

        assertEquals(1, generator.generate())
        assertEquals(2, generator.generate())
        assertEquals(1, generator.generate())
    }

    @Test
    fun `gives up redrawing instead of looping forever`() {
        val generator = QueueTaskGenerator(MutableList(200) { 5 }, repetitionWindow = 1)

        assertEquals(5, generator.generate())
        assertEquals(5, generator.generate())
    }

    @Test
    fun `a window of zero never checks for repeats`() {
        val generator = QueueTaskGenerator(mutableListOf(7, 7, 7), repetitionWindow = 0)

        assertEquals(7, generator.generate())
        assertEquals(7, generator.generate())
        assertEquals(7, generator.generate())
    }

    @Test
    fun `rejects a negative repetition window`() {
        assertFailsWith<IllegalArgumentException> {
            QueueTaskGenerator(mutableListOf(), repetitionWindow = -1)
        }
    }
}
