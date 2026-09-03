package de.vegnpunk.mentalmatics.ui.session

sealed interface SessionEvent {
    data class AnswerChanged(
        val answer: String,
    ) : SessionEvent

    data object SubmitAnswer : SessionEvent

    data object NextTask : SessionEvent
}
