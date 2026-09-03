package de.vegnpunk.mentalmatics.ui.difficultyselection

import de.vegnpunk.mentalmatics.core.generation.Difficulty

data class DifficultySelectionUiState(
    val availableDifficulties: List<Difficulty> = Difficulty.entries,
    val selectedDifficulty: Difficulty = Difficulty.MEDIUM,
)
