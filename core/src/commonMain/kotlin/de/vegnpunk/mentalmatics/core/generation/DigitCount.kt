package de.vegnpunk.mentalmatics.core.generation

/**
 * Number of digits the generated operands should have (US-2.2),
 * directly determining the number range instead of a named difficulty
 * level (e.g. "easy"/"medium"/"hard"). Exercise areas map this to
 * their own [DifficultyConfig] (see e.g. `core.arithmetic`).
 *
 * Not `@Serializable`: androidx.navigation's type-safe routes only
 * support primitives/enums/lists thereof as arguments, so this never
 * travels as a route argument directly — callers pass the raw
 * `digits: Int` through a route and reconstruct [DigitCount] on the
 * other side (see `docs/negative-knowledge.md`).
 */
data class DigitCount(
    val digits: Int,
) {
    init {
        require(digits in 1..MAX_DIGITS) { "digits must be between 1 and $MAX_DIGITS, was $digits." }
    }

    companion object {
        const val MAX_DIGITS = 6
    }
}
