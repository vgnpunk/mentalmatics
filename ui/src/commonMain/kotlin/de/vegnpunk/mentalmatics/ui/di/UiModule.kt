package de.vegnpunk.mentalmatics.ui.di

import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticOperation
import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticTaskGenerator
import de.vegnpunk.mentalmatics.core.arithmetic.toArithmeticDifficultyConfig
import de.vegnpunk.mentalmatics.core.generation.DigitCount
import de.vegnpunk.mentalmatics.core.generation.SessionFeedbackMode
import de.vegnpunk.mentalmatics.core.generation.SessionLength
import de.vegnpunk.mentalmatics.core.selection.OperationSelection
import de.vegnpunk.mentalmatics.ui.session.SessionViewModel
import de.vegnpunk.mentalmatics.ui.setup.SetupViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private const val SESSION_REPETITION_WINDOW = 5

/** Koin module for `:ui` (ADR-007): one entry per screen's view model. */
val uiModule =
    module {
        viewModel { SetupViewModel() }
        viewModel {
            (
                selectedOperations: Set<ArithmeticOperation>,
                digitCount: DigitCount,
                sessionLength: SessionLength,
                feedbackMode: SessionFeedbackMode,
            ),
            ->
            SessionViewModel(
                taskGenerator =
                    ArithmeticTaskGenerator(
                        operationSelection = OperationSelection(selectedOperations),
                        difficultyConfig = digitCount.toArithmeticDifficultyConfig(),
                        repetitionWindow = SESSION_REPETITION_WINDOW,
                    ),
                sessionLength = sessionLength,
                feedbackMode = feedbackMode,
            )
        }
    }
