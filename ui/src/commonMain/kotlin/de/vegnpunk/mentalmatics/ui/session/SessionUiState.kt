package de.vegnpunk.mentalmatics.ui.session

import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticSessionReport
import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticTask
import de.vegnpunk.mentalmatics.core.generation.SessionLength

/**
 * US-5.2: no per-task feedback, so the screen has exactly two phases —
 * solving tasks, then a report — instead of a "feedback" flag on a
 * single flat state.
 */
sealed interface SessionUiState {
    data class InProgress(
        val task: ArithmeticTask,
        val answerInput: String,
        val completedCount: Int,
        val sessionLength: SessionLength,
        val elapsedSeconds: Int,
        val lastAttemptIncorrect: Boolean = false,
    ) : SessionUiState

    data class Completed(
        val report: ArithmeticSessionReport,
    ) : SessionUiState
}
