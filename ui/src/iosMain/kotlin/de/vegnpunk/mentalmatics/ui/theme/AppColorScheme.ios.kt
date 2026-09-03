package de.vegnpunk.mentalmatics.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// iOS has no OS-level API to derive a color scheme from the system wallpaper/theme.
@Composable
actual fun appColorScheme(darkTheme: Boolean): ColorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()
