package de.vegnpunk.mentalmatics.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import de.vegnpunk.mentalmatics.core.generation.DigitCount
import de.vegnpunk.mentalmatics.core.generation.SessionLength
import de.vegnpunk.mentalmatics.ui.session.SessionScreen
import de.vegnpunk.mentalmatics.ui.setup.SetupScreen

@Composable
fun MentalmaticsNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Route.Setup) {
        composable<Route.Setup> {
            SetupScreen(onNavigate = { route -> navController.navigate(route) })
        }
        composable<Route.Session> { backStackEntry ->
            val route: Route.Session = backStackEntry.toRoute()
            val sessionLength =
                when (route.sessionLengthType) {
                    SessionLengthType.TASK_COUNT -> SessionLength.TaskCount(route.sessionLengthValue)
                    SessionLengthType.DURATION -> SessionLength.Duration(route.sessionLengthValue)
                }
            SessionScreen(
                selectedOperations = route.selectedOperations.toSet(),
                digitCount = DigitCount(route.digitCount),
                sessionLength = sessionLength,
                feedbackMode = route.feedbackMode,
                onFinished = { navController.popBackStack() },
            )
        }
    }
}
