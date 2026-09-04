package de.vegnpunk.mentalmatics.ui.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticOperation
import de.vegnpunk.mentalmatics.ui.navigation.Route
import de.vegnpunk.mentalmatics.ui.navigation.SessionLengthType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val SECONDS_PER_MINUTE = 60

/**
 * Combined practice-session setup (US-1.1/US-2.1/US-2.2/US-5.1):
 * operation types, digit count, and session length, all on one screen.
 * Follows the StateFlow/UiState/Event pattern from ADR-010.
 */
class SetupViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SetupUiState())
    val uiState: StateFlow<SetupUiState> = _uiState.asStateFlow()

    private val _navigationEffect = Channel<Route>(Channel.BUFFERED)
    val navigationEffect: Flow<Route> = _navigationEffect.receiveAsFlow()

    fun onEvent(event: SetupEvent) {
        when (event) {
            is SetupEvent.ToggleOperation -> toggleOperation(event.operation)
            is SetupEvent.SelectDigitCount -> _uiState.update { it.copy(digitCount = event.digits) }
            is SetupEvent.SelectSessionLengthType -> _uiState.update { it.copy(sessionLengthType = event.type) }
            is SetupEvent.SelectTaskCount -> _uiState.update { it.copy(taskCount = event.count) }
            is SetupEvent.SelectDurationMinutes -> _uiState.update { it.copy(durationMinutes = event.minutes) }
            is SetupEvent.SelectFeedbackMode -> _uiState.update { it.copy(feedbackMode = event.mode) }
            SetupEvent.Start -> start()
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

    private fun start() {
        val state = _uiState.value
        val route =
            Route.Session(
                selectedOperations = state.selectedOperations.toList(),
                digitCount = state.digitCount,
                sessionLengthType = state.sessionLengthType,
                sessionLengthValue =
                    when (state.sessionLengthType) {
                        SessionLengthType.TASK_COUNT -> state.taskCount
                        SessionLengthType.DURATION -> state.durationMinutes * SECONDS_PER_MINUTE
                    },
                feedbackMode = state.feedbackMode,
            )
        viewModelScope.launch { _navigationEffect.send(route) }
    }
}
