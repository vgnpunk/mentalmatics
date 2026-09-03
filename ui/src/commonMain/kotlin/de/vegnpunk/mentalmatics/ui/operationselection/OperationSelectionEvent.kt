package de.vegnpunk.mentalmatics.ui.operationselection

import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticOperation

sealed interface OperationSelectionEvent {
    data class ToggleOperation(
        val operation: ArithmeticOperation,
    ) : OperationSelectionEvent
}
