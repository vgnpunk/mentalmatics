package de.vegnpunk.mentalmatics.ui.session

import androidx.lifecycle.ViewModel
import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticTask
import de.vegnpunk.mentalmatics.core.generation.TaskGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Free-practice session state (US-2.1/US-2.2/US-5.1 — free practice
 * only, no timer in the MVP), following the StateFlow/UiState/Event
 * pattern from ADR-010.
 *
 * Takes a [TaskGenerator] rather than constructing an
 * `ArithmeticTaskGenerator` itself (ADR-008: manual fakes over
 * mocking), so tests can supply a fixed sequence of tasks.
 */
class SessionViewModel(
    private val taskGenerator: TaskGenerator<ArithmeticTask>,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SessionUiState(task = taskGenerator.generate()))
    val uiState: StateFlow<SessionUiState> = _uiState.asStateFlow()

    fun onEvent(event: SessionEvent) {
        when (event) {
            is SessionEvent.AnswerChanged -> updateAnswer(event.answer)
            SessionEvent.SubmitAnswer -> submitAnswer()
            SessionEvent.NextTask -> nextTask()
        }
    }

    private fun updateAnswer(answer: String) {
        _uiState.update { it.copy(answerInput = answer) }
    }

    private fun submitAnswer() {
        _uiState.update { state ->
            val isCorrect = state.answerInput.toIntOrNull() == state.task.result
            state.copy(feedback = if (isCorrect) AnswerFeedback.CORRECT else AnswerFeedback.INCORRECT)
        }
    }

    private fun nextTask() {
        _uiState.update { SessionUiState(task = taskGenerator.generate()) }
    }
}
