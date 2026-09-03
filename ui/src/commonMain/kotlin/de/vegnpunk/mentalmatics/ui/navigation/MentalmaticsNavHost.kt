package de.vegnpunk.mentalmatics.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.vegnpunk.mentalmatics.ui.home.HomeScreen

@Composable
fun MentalmaticsNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Route.Home) {
        composable<Route.Home> { HomeScreen() }
    }
}
