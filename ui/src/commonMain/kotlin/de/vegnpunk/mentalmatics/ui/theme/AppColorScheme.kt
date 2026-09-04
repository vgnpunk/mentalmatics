package de.vegnpunk.mentalmatics.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

/**
 * Dynamic (Material You) color where the platform can derive it from
 * the system wallpaper/theme (US-8.3); a sensible static Material3
 * baseline scheme everywhere else (iOS has no OS-level API for this).
 */
@Composable
expect fun appColorScheme(darkTheme: Boolean): ColorScheme
