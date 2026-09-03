package de.vegnpunk.mentalmatics.ui.session

sealed interface SessionEvent {
    data class DigitPressed(
        val digit: Int,
    ) : SessionEvent

    data object BackspacePressed : SessionEvent

    data object ConfirmPressed : SessionEvent
}
