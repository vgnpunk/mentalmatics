package de.vegnpunk.mentalmatics.ui.operationselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticOperation
import de.vegnpunk.mentalmatics.ui.navigation.Route
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Multi-select operation-type state for the practice-session setup
 * (US-1.1), following the StateFlow/UiState/Event pattern from ADR-010.
 */
class OperationSelectionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(OperationSelectionUiState())
    val uiState: StateFlow<OperationSelectionUiState> = _uiState.asStateFlow()

    private val _navigationEffect = Channel<Route>(Channel.BUFFERED)
    val navigationEffect: Flow<Route> = _navigationEffect.receiveAsFlow()

    fun onEvent(event: OperationSelectionEvent) {
        when (event) {
            is OperationSelectionEvent.ToggleOperation -> toggleOperation(event.operation)
            OperationSelectionEvent.Continue -> navigateToDifficultySelection()
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

    private fun navigateToDifficultySelection() {
        viewModelScope.launch {
            _navigationEffect.send(Route.DifficultySelection)
        }
    }
}
