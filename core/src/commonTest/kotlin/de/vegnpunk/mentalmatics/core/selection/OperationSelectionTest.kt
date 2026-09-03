package de.vegnpunk.mentalmatics.core.selection

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private enum class TestOperation { A, B, C }

class OperationSelectionTest {
    @Test
    fun `rejects an empty selection`() {
        assertFailsWith<IllegalArgumentException> {
            OperationSelection<TestOperation>(emptySet())
        }
    }

    @Test
    fun `contains reflects the enabled set`() {
        val selection = OperationSelection(setOf(TestOperation.A, TestOperation.B))

        assertTrue(selection.contains(TestOperation.A))
        assertTrue(selection.contains(TestOperation.B))
        assertFalse(selection.contains(TestOperation.C))
    }

    @Test
    fun `random only returns enabled types`() {
        val selection = OperationSelection(setOf(TestOperation.A, TestOperation.C))
        val random = Random(42)

        repeat(50) {
            assertTrue(selection.contains(selection.random(random)))
        }
    }

    @Test
    fun `random with a single enabled type always returns that type`() {
        val selection = OperationSelection(setOf(TestOperation.B))

        assertEquals(TestOperation.B, selection.random())
    }
}
