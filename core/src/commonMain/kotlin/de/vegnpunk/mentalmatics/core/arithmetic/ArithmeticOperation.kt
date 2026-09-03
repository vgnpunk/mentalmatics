package de.vegnpunk.mentalmatics.core.arithmetic

import kotlinx.serialization.Serializable

/** The four basic operations selectable per US-2.1 (via `OperationSelection`). */
@Serializable
enum class ArithmeticOperation { ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION }
