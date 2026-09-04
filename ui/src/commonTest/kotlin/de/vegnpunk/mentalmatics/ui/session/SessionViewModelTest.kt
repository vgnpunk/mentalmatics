package de.vegnpunk.mentalmatics.ui.session

import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticOperation
import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticTask
import de.vegnpunk.mentalmatics.core.generation.SessionFeedbackMode
import de.vegnpunk.mentalmatics.core.generation.SessionLength
import de.vegnpunk.mentalmatics.core.generation.TaskGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Returns tasks from a fixed queue, so tests control exactly what's generated next. */
private class FakeTaskGenerator(
    private val tasks: MutableList<ArithmeticTask>,
) : TaskGenerator<ArithmeticTask> {
    override fun generate(): ArithmeticTask = tasks.removeFirst()
}

private val taskA = ArithmeticTask(listOf(7, 5), ArithmeticOperation.ADDITION, 12)
private val taskB = ArithmeticTask(listOf(9, 3), ArithmeticOperation.ADDITION, 12)

@OptIn(ExperimentalCoroutinesApi::class)
class SessionViewModelTest {
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
    fun `starts in progress with the first generated task`() {
        val viewModel = SessionViewModel(FakeTaskGenerator(mutableListOf(taskA)), SessionLength.TaskCount(3))

        val state = assertIs<SessionUiState.InProgress>(viewModel.uiState.value)
        assertEquals(taskA, state.task)
        assertEquals("", state.answerInput)
        assertEquals(0, state.completedCount)
    }

    @Test
    fun `digit presses build up the answer, backspace removes the last digit`() {
        val viewModel = SessionViewModel(FakeTaskGenerator(mutableListOf(taskA)), SessionLength.TaskCount(3))

        viewModel.onEvent(SessionEvent.DigitPressed(1))
        viewModel.onEvent(SessionEvent.DigitPressed(2))
        assertEquals("12", (viewModel.uiState.value as SessionUiState.InProgress).answerInput)

        viewModel.onEvent(SessionEvent.BackspacePressed)
        assertEquals("1", (viewModel.uiState.value as SessionUiState.InProgress).answerInput)
    }

    @Test
    fun `confirming with an empty answer does nothing`() {
        val viewModel = SessionViewModel(FakeTaskGenerator(mutableListOf(taskA)), SessionLength.TaskCount(3))

        viewModel.onEvent(SessionEvent.ConfirmPressed)

        val state = assertIs<SessionUiState.InProgress>(viewModel.uiState.value)
        assertEquals(taskA, state.task)
        assertEquals(0, state.completedCount)
    }

    @Test
    fun `confirming advances to the next task without exposing correctness`() {
        val viewModel = SessionViewModel(FakeTaskGenerator(mutableListOf(taskA, taskB)), SessionLength.TaskCount(2))

        viewModel.onEvent(SessionEvent.DigitPressed(1))
        viewModel.onEvent(SessionEvent.DigitPressed(2))
        viewModel.onEvent(SessionEvent.ConfirmPressed)

        val state = assertIs<SessionUiState.InProgress>(viewModel.uiState.value)
        assertEquals(taskB, state.task)
        assertEquals("", state.answerInput)
        assertEquals(1, state.completedCount)
    }

    @Test
    fun `reaching the task count shows a report with correct and incorrect results`() {
        val viewModel = SessionViewModel(FakeTaskGenerator(mutableListOf(taskA, taskB)), SessionLength.TaskCount(2))

        viewModel.onEvent(SessionEvent.DigitPressed(1))
        viewModel.onEvent(SessionEvent.DigitPressed(2))
        viewModel.onEvent(SessionEvent.ConfirmPressed) // correct: 12

        viewModel.onEvent(SessionEvent.DigitPressed(9))
        viewModel.onEvent(SessionEvent.ConfirmPressed) // wrong: 9

        val state = assertIs<SessionUiState.Completed>(viewModel.uiState.value)
        assertEquals(2, state.report.totalCount)
        assertEquals(1, state.report.correctCount)
    }

    @Test
    fun `retry-until-correct mode keeps the same task and clears the input on a wrong answer`() {
        val viewModel =
            SessionViewModel(
                FakeTaskGenerator(mutableListOf(taskA)),
                SessionLength.TaskCount(3),
                SessionFeedbackMode.RETRY_UNTIL_CORRECT,
            )

        viewModel.onEvent(SessionEvent.DigitPressed(9))
        viewModel.onEvent(SessionEvent.ConfirmPressed)

        val state = assertIs<SessionUiState.InProgress>(viewModel.uiState.value)
        assertEquals(taskA, state.task)
        assertEquals("", state.answerInput)
        assertEquals(0, state.completedCount)
        assertEquals(true, state.lastAttemptIncorrect)
    }

    @Test
    fun `retry-until-correct mode advances only once the answer is correct`() {
        val viewModel =
            SessionViewModel(
                FakeTaskGenerator(mutableListOf(taskA, taskB)),
                SessionLength.TaskCount(2),
                SessionFeedbackMode.RETRY_UNTIL_CORRECT,
            )

        viewModel.onEvent(SessionEvent.DigitPressed(9))
        viewModel.onEvent(SessionEvent.ConfirmPressed) // wrong, stays on taskA

        viewModel.onEvent(SessionEvent.DigitPressed(1))
        viewModel.onEvent(SessionEvent.DigitPressed(2))
        viewModel.onEvent(SessionEvent.ConfirmPressed) // correct: 12

        val state = assertIs<SessionUiState.InProgress>(viewModel.uiState.value)
        assertEquals(taskB, state.task)
        assertEquals(1, state.completedCount)
        assertEquals(false, state.lastAttemptIncorrect)
    }

    @Test
    fun `a duration session finishes once time is up, even without answering`() =
        runTest(testDispatcher) {
            val viewModel = SessionViewModel(FakeTaskGenerator(mutableListOf(taskA)), SessionLength.Duration(3))

            testDispatcher.scheduler.advanceTimeBy(3_100)
            testDispatcher.scheduler.runCurrent()

            val state = assertIs<SessionUiState.Completed>(viewModel.uiState.value)
            assertEquals(0, state.report.totalCount)
        }
}
