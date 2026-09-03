package de.vegnpunk.mentalmatics.ui.operationselection

import androidx.lifecycle.ViewModel
import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticOperation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Multi-select operation-type state for the practice-session setup
 * (US-1.1), following the StateFlow/UiState/Event pattern from ADR-010.
 */
class OperationSelectionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(OperationSelectionUiState())
    val uiState: StateFlow<OperationSelectionUiState> = _uiState.asStateFlow()

    fun onEvent(event: OperationSelectionEvent) {
        when (event) {
            is OperationSelectionEvent.ToggleOperation -> toggleOperation(event.operation)
        }
    }

    private fun toggleOperation(operation: ArithmeticOperation) {
        _uiState.update { state ->
            val selected = state.selectedOperations
            val updated = if (operation in selected) selected - operation else selected + operation
            // Mirrors OperationSelection's own invariant (:core): at least one type stays enabled.
            if (updated.isEmpty()) state else state.copy(selectedOperations = updated)
        }
    }
}
