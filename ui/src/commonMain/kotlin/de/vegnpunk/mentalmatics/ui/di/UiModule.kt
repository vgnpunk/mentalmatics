package de.vegnpunk.mentalmatics.ui.di

import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticOperation
import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticTaskGenerator
import de.vegnpunk.mentalmatics.core.arithmetic.toArithmeticDifficultyConfig
import de.vegnpunk.mentalmatics.core.generation.Difficulty
import de.vegnpunk.mentalmatics.core.selection.OperationSelection
import de.vegnpunk.mentalmatics.ui.difficultyselection.DifficultySelectionViewModel
import de.vegnpunk.mentalmatics.ui.operationselection.OperationSelectionViewModel
import de.vegnpunk.mentalmatics.ui.session.SessionViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private const val SESSION_REPETITION_WINDOW = 5

/** Koin module for `:ui` (ADR-007): one entry per screen's view model. */
val uiModule =
    module {
        viewModel { OperationSelectionViewModel() }
        viewModel { (selectedOperations: Set<ArithmeticOperation>) ->
            DifficultySelectionViewModel(selectedOperations)
        }
        viewModel { (selectedOperations: Set<ArithmeticOperation>, selectedDifficulty: Difficulty) ->
            SessionViewModel(
                taskGenerator =
                    ArithmeticTaskGenerator(
                        operationSelection = OperationSelection(selectedOperations),
                        difficultyConfig = selectedDifficulty.toArithmeticDifficultyConfig(),
                        repetitionWindow = SESSION_REPETITION_WINDOW,
                    ),
            )
        }
    }
