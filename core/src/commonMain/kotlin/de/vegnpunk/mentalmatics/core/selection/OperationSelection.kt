package de.vegnpunk.mentalmatics.core.selection

import kotlin.random.Random

/**
 * The enabled subset of an exercise area's operation types (US-1.1):
 * any combination from "only one type" to "all types mixed".
 *
 * Generic over the exercise area's own operation-type enum, so every
 * `TaskGenerator` (ADR-014) reuses this instead of re-implementing
 * selection per exercise area.
 */
data class OperationSelection<T>(
    val enabled: Set<T>,
) {
    init {
        require(enabled.isNotEmpty()) { "At least one operation type must be enabled." }
    }

    fun contains(type: T): Boolean = type in enabled

    /** Picks one of the enabled operation types at random. */
    fun random(random: Random = Random.Default): T = enabled.random(random)
}
