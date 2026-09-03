package de.vegnpunk.mentalmatics.ui.difficultyselection

import de.vegnpunk.mentalmatics.core.generation.Difficulty

sealed interface DifficultySelectionEvent {
    data class SelectDifficulty(
        val difficulty: Difficulty,
    ) : DifficultySelectionEvent
}
