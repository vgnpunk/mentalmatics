package de.vegnpunk.mentalmatics.ui.navigation

import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticOperation
import de.vegnpunk.mentalmatics.core.generation.SessionFeedbackMode
import kotlinx.serialization.Serializable

/**
 * Flattened counterpart of `core.generation.SessionLength`: navigation
 * routes only support primitives/enums/lists thereof as arguments (see
 * `docs/negative-knowledge.md`), so [Route.Session] carries this enum
 * plus a raw `sessionLengthValue: Int` instead of the sealed
 * `SessionLength` type directly. Reconstructed in
 * [MentalmaticsNavHost].
 */
@Serializable
enum class SessionLengthType { TASK_COUNT, DURATION }

/**
 * Type-safe navigation destinations (ADR-011). A new screen adds a case
 * here and a matching `composable<Route.X>` entry in [MentalmaticsNavHost].
 *
 * Screens trigger navigation as a one-shot effect from their view model
 * (ADR-010), not directly from the composable — e.g. a `Channel<Route>`
 * the screen collects and forwards to `navController.navigate(...)`.
 */
sealed interface Route {
    @Serializable
    data object Setup : Route

    @Serializable
    data class Session(
        val selectedOperations: List<ArithmeticOperation>,
        val digitCount: Int,
        val sessionLengthType: SessionLengthType,
        val sessionLengthValue: Int,
        val feedbackMode: SessionFeedbackMode,
    ) : Route
}
