package de.vegnpunk.mentalmatics.core.arithmetic

/** One answered task (US-5.2): what was asked, what the user answered, and whether that was right. */
data class ArithmeticTaskResult(
    val task: ArithmeticTask,
    val userAnswer: Int?,
    val isCorrect: Boolean,
)

/** End-of-session summary (US-5.2) shown instead of per-task feedback. */
data class ArithmeticSessionReport(
    val results: List<ArithmeticTaskResult>,
) {
    val correctCount: Int get() = results.count { it.isCorrect }
    val totalCount: Int get() = results.size
}
