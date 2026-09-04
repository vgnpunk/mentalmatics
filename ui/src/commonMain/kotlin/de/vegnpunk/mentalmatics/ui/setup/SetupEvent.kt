package de.vegnpunk.mentalmatics.ui.setup

import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticOperation
import de.vegnpunk.mentalmatics.core.generation.SessionFeedbackMode
import de.vegnpunk.mentalmatics.ui.navigation.SessionLengthType

sealed interface SetupEvent {
    data class ToggleOperation(
        val operation: ArithmeticOperation,
    ) : SetupEvent

    data class SelectDigitCount(
        val digits: Int,
    ) : SetupEvent

    data class SelectSessionLengthType(
        val type: SessionLengthType,
    ) : SetupEvent

    data class SelectTaskCount(
        val count: Int,
    ) : SetupEvent

    data class SelectDurationMinutes(
        val minutes: Int,
    ) : SetupEvent

    data class SelectFeedbackMode(
        val mode: SessionFeedbackMode,
    ) : SetupEvent

    data object Start : SetupEvent
}
