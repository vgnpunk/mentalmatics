package de.vegnpunk.mentalmatics.ui.difficultyselection

import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticOperation
import de.vegnpunk.mentalmatics.core.generation.Difficulty
import de.vegnpunk.mentalmatics.ui.navigation.Route
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class DifficultySelectionViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val someOperations = setOf(ArithmeticOperation.ADDITION, ArithmeticOperation.SUBTRACTION)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `starts on medium difficulty`() {
        val viewModel = DifficultySelectionViewModel(someOperations)

        assertEquals(Difficulty.MEDIUM, viewModel.uiState.value.selectedDifficulty)
    }

    @Test
    fun `selecting a difficulty updates the state`() {
        val viewModel = DifficultySelectionViewModel(someOperations)

        viewModel.onEvent(DifficultySelectionEvent.SelectDifficulty(Difficulty.HARD))

        assertEquals(Difficulty.HARD, viewModel.uiState.value.selectedDifficulty)
    }

    @Test
    fun `continue emits a navigation effect carrying the operations and difficulty`() =
        runTest(testDispatcher) {
            val viewModel = DifficultySelectionViewModel(someOperations)
            viewModel.onEvent(DifficultySelectionEvent.SelectDifficulty(Difficulty.HARD))

            viewModel.onEvent(DifficultySelectionEvent.Continue)

            val expected = Route.Session(selectedOperations = someOperations, selectedDifficulty = Difficulty.HARD)
            assertEquals(expected, viewModel.navigationEffect.first())
        }
}
