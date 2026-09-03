package de.vegnpunk.mentalmatics.ui.operationselection

import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticOperation
import mentalmatics.ui.generated.resources.Res
import mentalmatics.ui.generated.resources.operation_addition
import mentalmatics.ui.generated.resources.operation_division
import mentalmatics.ui.generated.resources.operation_multiplication
import mentalmatics.ui.generated.resources.operation_subtraction
import org.jetbrains.compose.resources.StringResource

val ArithmeticOperation.labelRes: StringResource
    get() =
        when (this) {
            ArithmeticOperation.ADDITION -> Res.string.operation_addition
            ArithmeticOperation.SUBTRACTION -> Res.string.operation_subtraction
            ArithmeticOperation.MULTIPLICATION -> Res.string.operation_multiplication
            ArithmeticOperation.DIVISION -> Res.string.operation_division
        }
