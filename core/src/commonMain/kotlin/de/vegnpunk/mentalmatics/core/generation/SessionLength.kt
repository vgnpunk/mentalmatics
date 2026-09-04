package de.vegnpunk.mentalmatics.core.generation

/**
 * How long a practice session lasts (US-5.1): a fixed number of tasks,
 * or a fixed duration. Not `@Serializable` for the same reason as
 * [DigitCount] — never travels as a route argument directly.
 */
sealed interface SessionLength {
    fun isComplete(
        completedTaskCount: Int,
        elapsedSeconds: Int,
    ): Boolean

    data class TaskCount(
        val count: Int,
    ) : SessionLength {
        init {
            require(count >= 1) { "count must be at least 1, was $count." }
        }

        override fun isComplete(
            completedTaskCount: Int,
            elapsedSeconds: Int,
        ): Boolean = completedTaskCount >= count
    }

    data class Duration(
        val totalSeconds: Int,
    ) : SessionLength {
        init {
            require(totalSeconds >= 1) { "totalSeconds must be at least 1, was $totalSeconds." }
        }

        override fun isComplete(
            completedTaskCount: Int,
            elapsedSeconds: Int,
        ): Boolean = elapsedSeconds >= totalSeconds
    }
}
