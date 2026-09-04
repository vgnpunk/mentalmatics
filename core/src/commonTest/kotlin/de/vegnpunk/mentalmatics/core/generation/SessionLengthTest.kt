package de.vegnpunk.mentalmatics.core.generation

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SessionLengthTest {
    @Test
    fun `task count is complete once enough tasks are done, regardless of elapsed time`() {
        val length = SessionLength.TaskCount(count = 10)

        assertFalse(length.isComplete(completedTaskCount = 9, elapsedSeconds = 999_999))
        assertTrue(length.isComplete(completedTaskCount = 10, elapsedSeconds = 0))
    }

    @Test
    fun `duration is complete once enough time has passed, regardless of task count`() {
        val length = SessionLength.Duration(totalSeconds = 300)

        assertFalse(length.isComplete(completedTaskCount = 0, elapsedSeconds = 299))
        assertTrue(length.isComplete(completedTaskCount = 0, elapsedSeconds = 300))
    }

    @Test
    fun `rejects a non-positive task count`() {
        assertFailsWith<IllegalArgumentException> { SessionLength.TaskCount(0) }
    }

    @Test
    fun `rejects a non-positive duration`() {
        assertFailsWith<IllegalArgumentException> { SessionLength.Duration(0) }
    }
}
