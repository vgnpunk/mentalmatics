package de.vegnpunk.mentalmatics.ui.difficultyselection

import androidx.lifecycle.ViewModel
import de.vegnpunk.mentalmatics.core.generation.Difficulty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Single-select difficulty-level state for the practice-session setup
 * (US-2.2), following the StateFlow/UiState/Event pattern from ADR-010.
 */
class DifficultySelectionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DifficultySelectionUiState())
    val uiState: StateFlow<DifficultySelectionUiState> = _uiState.asStateFlow()

    fun onEvent(event: DifficultySelectionEvent) {
        when (event) {
            is DifficultySelectionEvent.SelectDifficulty -> selectDifficulty(event.difficulty)
        }
    }

    private fun selectDifficulty(difficulty: Difficulty) {
        _uiState.update { it.copy(selectedDifficulty = difficulty) }
    }
}
