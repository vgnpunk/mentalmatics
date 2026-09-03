package de.vegnpunk.mentalmatics.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import de.vegnpunk.mentalmatics.ui.difficultyselection.DifficultySelectionScreen
import de.vegnpunk.mentalmatics.ui.operationselection.OperationSelectionScreen
import de.vegnpunk.mentalmatics.ui.session.SessionScreen

@Composable
fun MentalmaticsNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Route.Home) {
        composable<Route.Home> {
            OperationSelectionScreen(onNavigate = { route -> navController.navigate(route) })
        }
        composable<Route.DifficultySelection> { backStackEntry ->
            val route: Route.DifficultySelection = backStackEntry.toRoute()
            DifficultySelectionScreen(
                selectedOperations = route.selectedOperations,
                onNavigate = { destination -> navController.navigate(destination) },
            )
        }
        composable<Route.Session> { backStackEntry ->
            val route: Route.Session = backStackEntry.toRoute()
            SessionScreen(
                selectedOperations = route.selectedOperations,
                selectedDifficulty = route.selectedDifficulty,
            )
        }
    }
}
