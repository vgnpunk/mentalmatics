package de.vegnpunk.mentalmatics.ui.session

import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticOperation
import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticTask
import de.vegnpunk.mentalmatics.core.generation.TaskGenerator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** Returns tasks from a fixed queue, so tests control exactly what's generated next. */
private class FakeTaskGenerator(
    private val tasks: MutableList<ArithmeticTask>,
) : TaskGenerator<ArithmeticTask> {
    override fun generate(): ArithmeticTask = tasks.removeFirst()
}

private val additionTask = ArithmeticTask(listOf(7, 5), ArithmeticOperation.ADDITION, 12)
private val subtractionTask = ArithmeticTask(listOf(12, 4), ArithmeticOperation.SUBTRACTION, 8)

class SessionViewModelTest {
    @Test
    fun `starts with the first generated task and no feedback`() {
        val viewModel = SessionViewModel(FakeTaskGenerator(mutableListOf(additionTask)))

        assertEquals(additionTask, viewModel.uiState.value.task)
        assertNull(viewModel.uiState.value.feedback)
    }

    @Test
    fun `answer changes update the input`() {
        val viewModel = SessionViewModel(FakeTaskGenerator(mutableListOf(additionTask)))

        viewModel.onEvent(SessionEvent.AnswerChanged("12"))

        assertEquals("12", viewModel.uiState.value.answerInput)
    }

    @Test
    fun `submitting the correct answer gives correct feedback`() {
        val viewModel = SessionViewModel(FakeTaskGenerator(mutableListOf(additionTask)))
        viewModel.onEvent(SessionEvent.AnswerChanged("12"))

        viewModel.onEvent(SessionEvent.SubmitAnswer)

        assertEquals(AnswerFeedback.CORRECT, viewModel.uiState.value.feedback)
    }

    @Test
    fun `submitting a wrong answer gives incorrect feedback`() {
        val viewModel = SessionViewModel(FakeTaskGenerator(mutableListOf(additionTask)))
        viewModel.onEvent(SessionEvent.AnswerChanged("13"))

        viewModel.onEvent(SessionEvent.SubmitAnswer)

        assertEquals(AnswerFeedback.INCORRECT, viewModel.uiState.value.feedback)
    }

    @Test
    fun `submitting a non-numeric answer gives incorrect feedback`() {
        val viewModel = SessionViewModel(FakeTaskGenerator(mutableListOf(additionTask)))
        viewModel.onEvent(SessionEvent.AnswerChanged("twelve"))

        viewModel.onEvent(SessionEvent.SubmitAnswer)

        assertEquals(AnswerFeedback.INCORRECT, viewModel.uiState.value.feedback)
    }

    @Test
    fun `moving to the next task clears the answer and feedback`() {
        val viewModel = SessionViewModel(FakeTaskGenerator(mutableListOf(additionTask, subtractionTask)))
        viewModel.onEvent(SessionEvent.AnswerChanged("12"))
        viewModel.onEvent(SessionEvent.SubmitAnswer)

        viewModel.onEvent(SessionEvent.NextTask)

        val state = viewModel.uiState.value
        assertEquals(subtractionTask, state.task)
        assertEquals("", state.answerInput)
        assertNull(state.feedback)
    }
}
