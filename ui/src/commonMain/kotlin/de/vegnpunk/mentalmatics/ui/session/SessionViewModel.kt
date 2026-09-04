package de.vegnpunk.mentalmatics.ui.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticSessionReport
import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticTask
import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticTaskResult
import de.vegnpunk.mentalmatics.core.generation.SessionFeedbackMode
import de.vegnpunk.mentalmatics.core.generation.SessionLength
import de.vegnpunk.mentalmatics.core.generation.TaskGenerator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TICK_MILLIS = 1_000L

/**
 * Free-practice session state (US-2.1/US-2.2/US-5.1/US-5.2/US-5.3):
 * no per-task feedback, answers are entered via an on-screen keypad,
 * and the session ends — showing a report — once [SessionLength] is
 * reached. Follows the StateFlow/UiState/Event pattern from ADR-010.
 *
 * Takes a [TaskGenerator] rather than constructing an
 * `ArithmeticTaskGenerator` itself (ADR-008: manual fakes over
 * mocking), so tests can supply a fixed sequence of tasks.
 */
class SessionViewModel(
    private val taskGenerator: TaskGenerator<ArithmeticTask>,
    private val sessionLength: SessionLength,
    private val feedbackMode: SessionFeedbackMode = SessionFeedbackMode.REPORT_AT_END,
) : ViewModel() {
    private val results = mutableListOf<ArithmeticTaskResult>()
    private var elapsedSeconds = 0

    private val _uiState = MutableStateFlow<SessionUiState>(initialState())
    val uiState: StateFlow<SessionUiState> = _uiState.asStateFlow()

    init {
        if (sessionLength is SessionLength.Duration) {
            startTicking()
        }
    }

    fun onEvent(event: SessionEvent) {
        val current = _uiState.value as? SessionUiState.InProgress ?: return
        when (event) {
            is SessionEvent.DigitPressed -> updateAnswer(current, current.answerInput + event.digit)
            SessionEvent.BackspacePressed -> updateAnswer(current, current.answerInput.dropLast(1))
            SessionEvent.ConfirmPressed -> confirmAnswer(current)
        }
    }

    private fun initialState() =
        SessionUiState.InProgress(
            task = taskGenerator.generate(),
            answerInput = "",
            completedCount = 0,
            sessionLength = sessionLength,
            elapsedSeconds = 0,
        )

    private fun updateAnswer(
        current: SessionUiState.InProgress,
        newInput: String,
    ) {
        _uiState.value = current.copy(answerInput = newInput, lastAttemptIncorrect = false)
    }

    private fun confirmAnswer(current: SessionUiState.InProgress) {
        if (current.answerInput.isEmpty()) return

        val userAnswer = current.answerInput.toIntOrNull()
        val isCorrect = userAnswer == current.task.result

        if (!isCorrect && feedbackMode == SessionFeedbackMode.RETRY_UNTIL_CORRECT) {
            _uiState.value = current.copy(answerInput = "", lastAttemptIncorrect = true)
            return
        }

        results += ArithmeticTaskResult(current.task, userAnswer, isCorrect)

        if (sessionLength.isComplete(results.size, elapsedSeconds)) {
            finishSession()
        } else {
            _uiState.value =
                current.copy(
                    task = taskGenerator.generate(),
                    answerInput = "",
                    completedCount = results.size,
                    lastAttemptIncorrect = false,
                )
        }
    }

    private fun startTicking() {
        viewModelScope.launch {
            while (_uiState.value is SessionUiState.InProgress) {
                delay(TICK_MILLIS)
                elapsedSeconds++
                if (sessionLength.isComplete(results.size, elapsedSeconds)) {
                    finishSession()
                } else {
                    _uiState.update { state ->
                        (state as? SessionUiState.InProgress)?.copy(elapsedSeconds = elapsedSeconds) ?: state
                    }
                }
            }
        }
    }

    private fun finishSession() {
        _uiState.value = SessionUiState.Completed(ArithmeticSessionReport(results.toList()))
    }
}
