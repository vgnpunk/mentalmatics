package de.vegnpunk.mentalmatics.ui.setup

import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticOperation
import de.vegnpunk.mentalmatics.core.generation.DigitCount
import de.vegnpunk.mentalmatics.core.generation.SessionFeedbackMode
import de.vegnpunk.mentalmatics.ui.navigation.SessionLengthType

data class SetupUiState(
    val availableOperations: List<ArithmeticOperation> = ArithmeticOperation.entries,
    val selectedOperations: Set<ArithmeticOperation> = ArithmeticOperation.entries.toSet(),
    val digitCount: Int = DEFAULT_DIGIT_COUNT,
    val sessionLengthType: SessionLengthType = SessionLengthType.TASK_COUNT,
    val taskCount: Int = DEFAULT_TASK_COUNT,
    val durationMinutes: Int = DEFAULT_DURATION_MINUTES,
    val feedbackMode: SessionFeedbackMode = SessionFeedbackMode.REPORT_AT_END,
) {
    companion object {
        const val DEFAULT_DIGIT_COUNT = 2
        const val DEFAULT_TASK_COUNT = 10
        const val DEFAULT_DURATION_MINUTES = 5

        val DIGIT_COUNT_OPTIONS = 1..DigitCount.MAX_DIGITS
        val TASK_COUNT_OPTIONS = listOf(5, 10, 20, 30)
        val DURATION_MINUTES_OPTIONS = listOf(1, 2, 5, 10)
    }
}
