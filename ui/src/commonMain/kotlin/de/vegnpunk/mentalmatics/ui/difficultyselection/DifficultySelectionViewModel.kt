package de.vegnpunk.mentalmatics.ui.difficultyselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticOperation
import de.vegnpunk.mentalmatics.core.generation.Difficulty
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
 * Single-select difficulty-level state for the practice-session setup
 * (US-2.2), following the StateFlow/UiState/Event pattern from ADR-010.
 */
class DifficultySelectionViewModel(
    private val selectedOperations: Set<ArithmeticOperation>,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DifficultySelectionUiState())
    val uiState: StateFlow<DifficultySelectionUiState> = _uiState.asStateFlow()

    private val _navigationEffect = Channel<Route>(Channel.BUFFERED)
    val navigationEffect: Flow<Route> = _navigationEffect.receiveAsFlow()

    fun onEvent(event: DifficultySelectionEvent) {
        when (event) {
            is DifficultySelectionEvent.SelectDifficulty -> selectDifficulty(event.difficulty)
            DifficultySelectionEvent.Continue -> navigateToSession()
        }
    }

    private fun selectDifficulty(difficulty: Difficulty) {
        _uiState.update { it.copy(selectedDifficulty = difficulty) }
    }

    private fun navigateToSession() {
        viewModelScope.launch {
            _navigationEffect.send(
                Route.Session(
                    selectedOperations = selectedOperations.toList(),
                    selectedDifficulty = _uiState.value.selectedDifficulty,
                ),
            )
        }
    }
}
