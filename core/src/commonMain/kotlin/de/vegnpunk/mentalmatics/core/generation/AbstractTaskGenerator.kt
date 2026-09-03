package de.vegnpunk.mentalmatics.core.generation

/**
 * Handles repetition avoidance (ADR-014) once for every [TaskGenerator]
 * implementation: a candidate from [createCandidate] is discarded and
 * redrawn while it's still in the last [repetitionWindow] generated
 * tasks. Falls back to accepting a repeat after [MAX_ATTEMPTS] draws,
 * so a [DifficultyConfig] whose value space is smaller than
 * [repetitionWindow] can't spin forever.
 */
abstract class AbstractTaskGenerator<Task>(
    private val repetitionWindow: Int,
) : TaskGenerator<Task> {
    init {
        require(repetitionWindow >= 0) { "repetitionWindow must not be negative, was $repetitionWindow." }
    }

    private val recentTasks = ArrayDeque<Task>(repetitionWindow)

    protected abstract fun createCandidate(): Task

    final override fun generate(): Task {
        var candidate = createCandidate()
        var attempts = 1
        while (candidate in recentTasks && attempts < MAX_ATTEMPTS) {
            candidate = createCandidate()
            attempts++
        }
        remember(candidate)
        return candidate
    }

    private fun remember(task: Task) {
        if (repetitionWindow == 0) return
        while (recentTasks.size >= repetitionWindow) {
            recentTasks.removeFirst()
        }
        recentTasks.addLast(task)
    }

    private companion object {
        const val MAX_ATTEMPTS = 100
    }
}
