package de.vegnpunk.mentalmatics.core.arithmetic

import de.vegnpunk.mentalmatics.core.generation.AbstractTaskGenerator
import de.vegnpunk.mentalmatics.core.generation.DifficultyConfig
import de.vegnpunk.mentalmatics.core.selection.OperationSelection
import kotlin.random.Random

/**
 * Generates arithmetic tasks (US-2.1/US-2.2) for the operations enabled
 * in [operationSelection], using [difficultyConfig] for number size and
 * operand count.
 *
 * Subtraction/division are built "from the result outward" (pick the
 * result and the trailing operands first, then derive the leading
 * operand) so a chain of any length ([DifficultyConfig.operandCount])
 * is non-negative / evenly divisible by construction, instead of
 * searching for a valid combination. The leading operand is resampled
 * up to [MAX_RANGE_ATTEMPTS] times if it falls outside
 * [DifficultyConfig.numberRange].
 */
class ArithmeticTaskGenerator(
    private val operationSelection: OperationSelection<ArithmeticOperation>,
    private val difficultyConfig: DifficultyConfig,
    repetitionWindow: Int,
    private val random: Random = Random.Default,
) : AbstractTaskGenerator<ArithmeticTask>(repetitionWindow) {
    override fun createCandidate(): ArithmeticTask {
        val operation = operationSelection.random(random)
        val operands = generateOperands(operation)
        return ArithmeticTask(operands, operation, evaluate(operands, operation))
    }

    private fun generateOperands(operation: ArithmeticOperation): List<Int> {
        val trailingCount = difficultyConfig.operandCount - 1
        return when (operation) {
            ArithmeticOperation.ADDITION, ArithmeticOperation.MULTIPLICATION ->
                List(difficultyConfig.operandCount) { randomOperand() }

            ArithmeticOperation.SUBTRACTION ->
                generateFromResult(trailingCount, ::randomOperand) { result, trailing -> result + trailing.sum() }

            ArithmeticOperation.DIVISION ->
                // Trailing operands are divisors at evaluation time, so they must never be zero.
                generateFromResult(trailingCount, ::randomNonZeroOperand) { result, trailing ->
                    result * trailing.fold(1) { acc, value -> acc * value }
                }
        }
    }

    private fun generateFromResult(
        trailingCount: Int,
        generateTrailingOperand: () -> Int,
        deriveLeading: (result: Int, trailing: List<Int>) -> Int,
    ): List<Int> {
        var leading: Int
        var trailing: List<Int>
        var attempts = 0
        do {
            val result = randomOperand()
            trailing = List(trailingCount) { generateTrailingOperand() }
            leading = deriveLeading(result, trailing)
            attempts++
        } while (leading !in difficultyConfig.numberRange && attempts < MAX_RANGE_ATTEMPTS)
        return listOf(leading) + trailing
    }

    private fun randomOperand(): Int = random.nextInt(difficultyConfig.numberRange.first, difficultyConfig.numberRange.last + 1)

    private fun randomNonZeroOperand(): Int {
        var value = randomOperand()
        var attempts = 1
        while (value == 0 && attempts < MAX_RANGE_ATTEMPTS) {
            value = randomOperand()
            attempts++
        }
        return value
    }

    private fun evaluate(
        operands: List<Int>,
        operation: ArithmeticOperation,
    ): Int =
        when (operation) {
            ArithmeticOperation.ADDITION -> operands.sum()
            ArithmeticOperation.SUBTRACTION -> operands.reduce { acc, value -> acc - value }
            ArithmeticOperation.MULTIPLICATION -> operands.reduce { acc, value -> acc * value }
            ArithmeticOperation.DIVISION -> operands.reduce { acc, value -> acc / value }
        }

    private companion object {
        const val MAX_RANGE_ATTEMPTS = 100
    }
}
