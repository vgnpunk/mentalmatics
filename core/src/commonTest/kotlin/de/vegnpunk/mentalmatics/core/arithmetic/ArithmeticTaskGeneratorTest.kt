package de.vegnpunk.mentalmatics.core.arithmetic

import de.vegnpunk.mentalmatics.core.generation.DifficultyConfig
import de.vegnpunk.mentalmatics.core.selection.OperationSelection
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ArithmeticTaskGeneratorTest {
    private fun generator(
        operations: Set<ArithmeticOperation>,
        numberRange: IntRange = 1..20,
        operandCount: Int = 2,
        seed: Int = 1,
    ) = ArithmeticTaskGenerator(
        operationSelection = OperationSelection(operations),
        difficultyConfig = DifficultyConfig(numberRange, operandCount),
        repetitionWindow = 0,
        random = Random(seed),
    )

    @Test
    fun `addition result is the sum of the operands`() {
        val generator = generator(setOf(ArithmeticOperation.ADDITION), operandCount = 3)

        repeat(50) {
            val task = generator.generate()
            assertEquals(ArithmeticOperation.ADDITION, task.operation)
            assertEquals(3, task.operands.size)
            assertEquals(task.operands.sum(), task.result)
        }
    }

    @Test
    fun `multiplication result is the product of the operands`() {
        val generator = generator(setOf(ArithmeticOperation.MULTIPLICATION), operandCount = 3)

        repeat(50) {
            val task = generator.generate()
            assertEquals(task.operands.reduce { acc, value -> acc * value }, task.result)
        }
    }

    @Test
    fun `subtraction never goes negative and stays within the number range`() {
        val range = 1..20
        val generator = generator(setOf(ArithmeticOperation.SUBTRACTION), numberRange = range, operandCount = 3)

        repeat(200) {
            val task = generator.generate()
            assertEquals(task.operands.reduce { acc, value -> acc - value }, task.result)
            assertTrue(task.result >= 0, "expected a non-negative result, got ${task.result} for ${task.operands}")
            assertTrue(task.operands.all { it in range }, "operand outside $range: ${task.operands}")
        }
    }

    @Test
    fun `division is always exact and divisors are never zero`() {
        val range = 1..20
        val generator = generator(setOf(ArithmeticOperation.DIVISION), numberRange = range, operandCount = 3)

        repeat(200) {
            val task = generator.generate()
            assertTrue(task.operands.drop(1).none { it == 0 }, "a divisor was zero: ${task.operands}")
            assertEquals(task.operands.reduce { acc, value -> acc / value }, task.result)
            // Exact division: multiplying back the quotient with every divisor recovers the dividend.
            assertEquals(task.operands.first(), task.operands.drop(1).fold(task.result) { acc, value -> acc * value })
        }
    }

    @Test
    fun `only generates operations enabled in the selection`() {
        val generator = generator(setOf(ArithmeticOperation.ADDITION, ArithmeticOperation.MULTIPLICATION))

        repeat(50) {
            val operation = generator.generate().operation
            assertTrue(operation == ArithmeticOperation.ADDITION || operation == ArithmeticOperation.MULTIPLICATION)
        }
    }

    @Test
    fun `same seed produces the same sequence of tasks`() {
        val first = generator(ArithmeticOperation.entries.toSet(), seed = 7)
        val second = generator(ArithmeticOperation.entries.toSet(), seed = 7)

        repeat(20) {
            assertEquals(first.generate(), second.generate())
        }
    }
}
