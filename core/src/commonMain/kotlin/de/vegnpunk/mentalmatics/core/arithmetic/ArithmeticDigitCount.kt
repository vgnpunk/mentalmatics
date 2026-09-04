package de.vegnpunk.mentalmatics.core.arithmetic

import de.vegnpunk.mentalmatics.core.generation.DifficultyConfig
import de.vegnpunk.mentalmatics.core.generation.DigitCount

/** Maps a digit count to the number range used for arithmetic tasks (US-2.2). */
fun DigitCount.toArithmeticDifficultyConfig(): DifficultyConfig {
    val upperBound = pow10(digits) - 1
    val lowerBound = if (digits == 1) 0 else pow10(digits - 1)
    return DifficultyConfig(numberRange = lowerBound..upperBound, operandCount = 2)
}

private fun pow10(exponent: Int): Int {
    var result = 1
    repeat(exponent) { result *= 10 }
    return result
}
