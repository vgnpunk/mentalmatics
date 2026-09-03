package de.vegnpunk.mentalmatics.ui.operationselection

import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticOperation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OperationSelectionViewModelTest {
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
}
