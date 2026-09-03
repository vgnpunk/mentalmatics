package de.vegnpunk.mentalmatics.ui.difficultyselection

import de.vegnpunk.mentalmatics.core.generation.Difficulty
import mentalmatics.ui.generated.resources.Res
import mentalmatics.ui.generated.resources.difficulty_easy
import mentalmatics.ui.generated.resources.difficulty_hard
import mentalmatics.ui.generated.resources.difficulty_medium
import org.jetbrains.compose.resources.StringResource

val Difficulty.labelRes: StringResource
    get() =
        when (this) {
            Difficulty.EASY -> Res.string.difficulty_easy
            Difficulty.MEDIUM -> Res.string.difficulty_medium
            Difficulty.HARD -> Res.string.difficulty_hard
        }
