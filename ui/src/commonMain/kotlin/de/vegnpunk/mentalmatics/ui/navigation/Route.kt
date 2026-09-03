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
 *
 * Operation selections travel as `List`, not `Set`: androidx.navigation's
 * type-safe routes only have built-in argument support for `List`/`Array`/
 * primitives, not `Set` (verified against androidx.navigation's
 * `NavTypeConverter` source — there is no `Set` case at all). Screens and
 * view models still work with `Set<ArithmeticOperation>`; the List/Set
 * conversion happens only at the route boundary (view models constructing
 * a `Route`, and [MentalmaticsNavHost] reading one back).
 */
sealed interface Route {
    @Serializable
    data object Home : Route

    @Serializable
    data class DifficultySelection(
        val selectedOperations: List<ArithmeticOperation>,
    ) : Route

    @Serializable
    data class Session(
        val selectedOperations: List<ArithmeticOperation>,
        val selectedDifficulty: Difficulty,
    ) : Route
}
