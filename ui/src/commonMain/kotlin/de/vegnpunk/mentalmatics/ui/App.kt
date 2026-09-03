package de.vegnpunk.mentalmatics.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import de.vegnpunk.mentalmatics.ui.navigation.MentalmaticsNavHost

@Composable
fun App() {
    MaterialTheme {
        MentalmaticsNavHost()
    }
}
