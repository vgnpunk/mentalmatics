package de.vegnpunk.mentalmatics.ui.operationselection

import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticOperation
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
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class OperationSelectionViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `starts with every operation selected`() {
        val viewModel = OperationSelectionViewModel()

        assertEquals(ArithmeticOperation.entries.toSet(), viewModel.uiState.value.selectedOperations)
    }

    @Test
    fun `toggling a selected operation deselects it`() {
        val viewModel = OperationSelectionViewModel()

        viewModel.onEvent(OperationSelectionEvent.ToggleOperation(ArithmeticOperation.DIVISION))

        assertTrue(ArithmeticOperation.DIVISION !in viewModel.uiState.value.selectedOperations)
    }

    @Test
    fun `toggling a deselected operation reselects it`() {
        val viewModel = OperationSelectionViewModel()
        viewModel.onEvent(OperationSelectionEvent.ToggleOperation(ArithmeticOperation.DIVISION))

        viewModel.onEvent(OperationSelectionEvent.ToggleOperation(ArithmeticOperation.DIVISION))

        assertTrue(ArithmeticOperation.DIVISION in viewModel.uiState.value.selectedOperations)
    }

    @Test
    fun `the last remaining operation cannot be deselected`() {
        val viewModel = OperationSelectionViewModel()
        val allButOne = ArithmeticOperation.entries.drop(1)
        allButOne.forEach { viewModel.onEvent(OperationSelectionEvent.ToggleOperation(it)) }
        val lastOperation = ArithmeticOperation.entries.first()
        check(viewModel.uiState.value.selectedOperations == setOf(lastOperation))

        viewModel.onEvent(OperationSelectionEvent.ToggleOperation(lastOperation))

        assertEquals(setOf(lastOperation), viewModel.uiState.value.selectedOperations)
    }

    @Test
    fun `continue emits a navigation effect to difficulty selection`() =
        runTest(testDispatcher) {
            val viewModel = OperationSelectionViewModel()

            viewModel.onEvent(OperationSelectionEvent.Continue)

            assertEquals(Route.DifficultySelection, viewModel.navigationEffect.first())
        }
}
