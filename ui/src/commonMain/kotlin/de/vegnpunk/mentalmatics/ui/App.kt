package de.vegnpunk.mentalmatics.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import de.vegnpunk.mentalmatics.ui.navigation.MentalmaticsNavHost
import de.vegnpunk.mentalmatics.ui.theme.appColorScheme

@Composable
fun App() {
    MaterialTheme(colorScheme = appColorScheme(darkTheme = isSystemInDarkTheme())) {
        MentalmaticsNavHost()
    }
}
