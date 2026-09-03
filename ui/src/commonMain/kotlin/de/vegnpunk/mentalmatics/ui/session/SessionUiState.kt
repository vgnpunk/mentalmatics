package de.vegnpunk.mentalmatics.ui.session

import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticTask

data class SessionUiState(
    val task: ArithmeticTask,
    val answerInput: String = "",
    val feedback: AnswerFeedback? = null,
)

enum class AnswerFeedback { CORRECT, INCORRECT }
