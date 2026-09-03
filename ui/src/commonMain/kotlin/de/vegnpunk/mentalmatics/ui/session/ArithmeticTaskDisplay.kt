package de.vegnpunk.mentalmatics.ui.session

import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticOperation
import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticTask

/** Math symbols are language-independent, so this needs no string resources. */
private val ArithmeticOperation.symbol: String
    get() =
        when (this) {
            ArithmeticOperation.ADDITION -> "+"
            ArithmeticOperation.SUBTRACTION -> "−"
            ArithmeticOperation.MULTIPLICATION -> "×"
            ArithmeticOperation.DIVISION -> "÷"
        }

/** E.g. `[364, 178]` + SUBTRACTION -> `"364 − 178 ="`. */
fun ArithmeticTask.toDisplayString(): String = operands.joinToString(separator = " ${operation.symbol} ") + " ="
