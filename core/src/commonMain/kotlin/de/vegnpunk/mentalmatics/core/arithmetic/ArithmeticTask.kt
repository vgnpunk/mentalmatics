package de.vegnpunk.mentalmatics.core.arithmetic

/** [operands] applied left to right with [operation], e.g. `[364, 178]` + SUBTRACTION -> 186. */
data class ArithmeticTask(
    val operands: List<Int>,
    val operation: ArithmeticOperation,
    val result: Int,
)
