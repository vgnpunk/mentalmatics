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
 * Subtraction is built "from the result outward" (pick the result and
 * the trailing operands first, then derive the leading operand) so a
 * chain of any length ([DifficultyConfig.operandCount]) is
 * non-negative by construction. The leading operand is resampled up to
 * [MAX_RANGE_ATTEMPTS] times if it falls outside
 * [DifficultyConfig.numberRange].
 *
 * Division is built forward instead (small divisors chosen first, then
 * a quotient within range, then the dividend derived from both) —
 * picking a dividend first and retrying, like subtraction does, risks
 * either overflow or near-guaranteed failure once the configured
 * number range spans more than a couple of digits.
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

            ArithmeticOperation.DIVISION -> generateDivisionOperands(trailingCount)
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

    private fun generateDivisionOperands(trailingCount: Int): List<Int> {
        val divisors = List(trailingCount) { randomDivisor() }
        val divisorProduct = divisors.fold(1L) { acc, value -> acc * value }
        val maxQuotient = difficultyConfig.numberRange.last / divisorProduct
        val minQuotient = difficultyConfig.numberRange.first / divisorProduct
        val quotient =
            if (maxQuotient <= minQuotient) {
                minQuotient
            } else {
                random.nextLong(minQuotient, maxQuotient + 1)
            }
        val dividend = (quotient * divisorProduct).toInt()
        return listOf(dividend) + divisors
    }

    private fun randomOperand(): Int = random.nextInt(difficultyConfig.numberRange.first, difficultyConfig.numberRange.last + 1)

    private fun randomDivisor(): Int = random.nextInt(MIN_DIVISOR, MAX_DIVISOR + 1)

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
        const val MIN_DIVISOR = 2
        const val MAX_DIVISOR = 12
    }
}
