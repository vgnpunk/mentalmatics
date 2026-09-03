package de.vegnpunk.mentalmatics.ui.di

import de.vegnpunk.mentalmatics.ui.difficultyselection.DifficultySelectionViewModel
import de.vegnpunk.mentalmatics.ui.operationselection.OperationSelectionViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/** Koin module for `:ui` (ADR-007): one entry per screen's view model. */
val uiModule =
    module {
        viewModel { OperationSelectionViewModel() }
        viewModel { DifficultySelectionViewModel() }
    }
