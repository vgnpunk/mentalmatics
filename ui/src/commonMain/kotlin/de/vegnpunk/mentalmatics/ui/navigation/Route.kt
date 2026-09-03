package de.vegnpunk.mentalmatics.ui.navigation

import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticOperation
import de.vegnpunk.mentalmatics.core.generation.Difficulty
import kotlinx.serialization.Serializable

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
    data object Home : Route

    @Serializable
    data class DifficultySelection(
        val selectedOperations: Set<ArithmeticOperation>,
    ) : Route

    @Serializable
    data class Session(
        val selectedOperations: Set<ArithmeticOperation>,
        val selectedDifficulty: Difficulty,
    ) : Route
}
