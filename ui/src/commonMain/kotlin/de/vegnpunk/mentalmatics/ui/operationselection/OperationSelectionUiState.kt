package de.vegnpunk.mentalmatics.ui.operationselection

import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticOperation

data class OperationSelectionUiState(
    val availableOperations: List<ArithmeticOperation> = ArithmeticOperation.entries,
    val selectedOperations: Set<ArithmeticOperation> = ArithmeticOperation.entries.toSet(),
)
