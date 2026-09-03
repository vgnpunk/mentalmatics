package de.vegnpunk.mentalmatics.ui

import androidx.compose.ui.window.ComposeUIViewController

// Uppercase to match the Kotlin/Native <-> Swift entry-point convention
// (invoked from Swift as `MainViewControllerKt.MainViewController()`).
@Suppress("ktlint:standard:function-naming")
fun MainViewController() = ComposeUIViewController { App() }
