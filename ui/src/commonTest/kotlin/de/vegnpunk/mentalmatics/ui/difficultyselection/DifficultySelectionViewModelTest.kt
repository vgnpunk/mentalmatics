package de.vegnpunk.mentalmatics.ui.difficultyselection

import de.vegnpunk.mentalmatics.core.generation.Difficulty
import kotlin.test.Test
import kotlin.test.assertEquals

class DifficultySelectionViewModelTest {
    @Test
    fun `starts on medium difficulty`() {
        val viewModel = DifficultySelectionViewModel()

        assertEquals(Difficulty.MEDIUM, viewModel.uiState.value.selectedDifficulty)
    }

    @Test
    fun `selecting a difficulty updates the state`() {
        val viewModel = DifficultySelectionViewModel()

        viewModel.onEvent(DifficultySelectionEvent.SelectDifficulty(Difficulty.HARD))

        assertEquals(Difficulty.HARD, viewModel.uiState.value.selectedDifficulty)
    }
}
