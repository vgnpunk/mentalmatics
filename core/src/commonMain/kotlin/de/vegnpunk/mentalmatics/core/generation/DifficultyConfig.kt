package de.vegnpunk.mentalmatics.core.generation

/**
 * Declarative difficulty parameters (US-2.2): number size and operand
 * count instead of hardcoded per-level branching in each generator
 * (ADR-014). Shared across exercise areas that generate numeric tasks.
 */
data class DifficultyConfig(
    val numberRange: IntRange,
    val operandCount: Int,
) {
    init {
        require(operandCount >= 2) { "operandCount must be at least 2, was $operandCount." }
        require(!numberRange.isEmpty()) { "numberRange must not be empty." }
    }
}
