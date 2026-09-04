package de.vegnpunk.mentalmatics.ui.setup

import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticOperation
import de.vegnpunk.mentalmatics.core.generation.SessionFeedbackMode
import de.vegnpunk.mentalmatics.ui.navigation.Route
import de.vegnpunk.mentalmatics.ui.navigation.SessionLengthType
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
class SetupViewModelTest {
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
    fun `starts with every operation selected and sensible defaults`() {
        val viewModel = SetupViewModel()

        val state = viewModel.uiState.value
        assertEquals(ArithmeticOperation.entries.toSet(), state.selectedOperations)
        assertEquals(SetupUiState.DEFAULT_DIGIT_COUNT, state.digitCount)
        assertEquals(SessionLengthType.TASK_COUNT, state.sessionLengthType)
        assertEquals(SessionFeedbackMode.REPORT_AT_END, state.feedbackMode)
    }

    @Test
    fun `selecting a feedback mode updates the state and the started route`() =
        runTest(testDispatcher) {
            val viewModel = SetupViewModel()

            viewModel.onEvent(SetupEvent.SelectFeedbackMode(SessionFeedbackMode.RETRY_UNTIL_CORRECT))
            assertEquals(SessionFeedbackMode.RETRY_UNTIL_CORRECT, viewModel.uiState.value.feedbackMode)

            viewModel.onEvent(SetupEvent.Start)

            val route = viewModel.navigationEffect.first() as Route.Session
            assertEquals(SessionFeedbackMode.RETRY_UNTIL_CORRECT, route.feedbackMode)
        }

    @Test
    fun `the last remaining operation cannot be deselected`() {
        val viewModel = SetupViewModel()
        val allButOne = ArithmeticOperation.entries.drop(1)
        allButOne.forEach { viewModel.onEvent(SetupEvent.ToggleOperation(it)) }
        val lastOperation = ArithmeticOperation.entries.first()

        viewModel.onEvent(SetupEvent.ToggleOperation(lastOperation))

        assertEquals(setOf(lastOperation), viewModel.uiState.value.selectedOperations)
    }

    @Test
    fun `selecting a digit count updates the state`() {
        val viewModel = SetupViewModel()

        viewModel.onEvent(SetupEvent.SelectDigitCount(5))

        assertEquals(5, viewModel.uiState.value.digitCount)
    }

    @Test
    fun `start emits a task-count session route by default`() =
        runTest(testDispatcher) {
            val viewModel = SetupViewModel()
            viewModel.onEvent(SetupEvent.ToggleOperation(ArithmeticOperation.DIVISION))
            viewModel.onEvent(SetupEvent.SelectDigitCount(3))

            viewModel.onEvent(SetupEvent.Start)

            val route = viewModel.navigationEffect.first() as Route.Session
            assertEquals(3, route.digitCount)
            assertEquals(SessionLengthType.TASK_COUNT, route.sessionLengthType)
            assertEquals(SetupUiState.DEFAULT_TASK_COUNT, route.sessionLengthValue)
            assertTrue(ArithmeticOperation.DIVISION !in route.selectedOperations)
        }

    @Test
    fun `start converts duration minutes to seconds`() =
        runTest(testDispatcher) {
            val viewModel = SetupViewModel()
            viewModel.onEvent(SetupEvent.SelectSessionLengthType(SessionLengthType.DURATION))
            viewModel.onEvent(SetupEvent.SelectDurationMinutes(2))

            viewModel.onEvent(SetupEvent.Start)

            val route = viewModel.navigationEffect.first() as Route.Session
            assertEquals(SessionLengthType.DURATION, route.sessionLengthType)
            assertEquals(120, route.sessionLengthValue)
        }
}
