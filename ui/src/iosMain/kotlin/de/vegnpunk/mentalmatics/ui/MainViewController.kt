package de.vegnpunk.mentalmatics.ui

import androidx.compose.ui.window.ComposeUIViewController
import de.vegnpunk.mentalmatics.ui.di.initKoin

// Uppercase to match the Kotlin/Native <-> Swift entry-point convention
// (invoked from Swift as `MainViewControllerKt.MainViewController()`).
@Suppress("ktlint:standard:function-naming")
fun MainViewController() = ComposeUIViewController { App() }.also { initKoin() }
