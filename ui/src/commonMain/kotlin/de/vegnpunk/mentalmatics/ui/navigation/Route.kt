package de.vegnpunk.mentalmatics.ui.navigation

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
    data object DifficultySelection : Route
}
