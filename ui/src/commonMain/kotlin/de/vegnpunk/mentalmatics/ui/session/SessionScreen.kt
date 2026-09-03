package de.vegnpunk.mentalmatics.ui.session

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticOperation
import de.vegnpunk.mentalmatics.core.generation.Difficulty
import mentalmatics.ui.generated.resources.Res
import mentalmatics.ui.generated.resources.action_check
import mentalmatics.ui.generated.resources.action_next_task
import mentalmatics.ui.generated.resources.session_answer_label
import mentalmatics.ui.generated.resources.session_feedback_correct
import mentalmatics.ui.generated.resources.session_feedback_incorrect
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SessionScreen(
    selectedOperations: Set<ArithmeticOperation>,
    selectedDifficulty: Difficulty,
    viewModel: SessionViewModel = koinViewModel { parametersOf(selectedOperations, selectedDifficulty) },
) {
    val uiState by viewModel.uiState.collectAsState()
    SessionContent(uiState = uiState, onEvent = viewModel::onEvent)
}

@Composable
private fun SessionContent(
    uiState: SessionUiState,
    onEvent: (SessionEvent) -> Unit,
) {
    val hasFeedback = uiState.feedback != null

    Column(
        modifier = Modifier.safeContentPadding().fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(text = uiState.task.toDisplayString(), style = MaterialTheme.typography.headlineMedium)
        TextField(
            value = uiState.answerInput,
            onValueChange = { onEvent(SessionEvent.AnswerChanged(it)) },
            enabled = !hasFeedback,
            label = { Text(stringResource(Res.string.session_answer_label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
        uiState.feedback?.let { feedback ->
            Text(
                when (feedback) {
                    AnswerFeedback.CORRECT -> stringResource(Res.string.session_feedback_correct)
                    AnswerFeedback.INCORRECT ->
                        stringResource(Res.string.session_feedback_incorrect, uiState.task.result)
                },
            )
        }
        Button(onClick = { onEvent(if (hasFeedback) SessionEvent.NextTask else SessionEvent.SubmitAnswer) }) {
            Text(stringResource(if (hasFeedback) Res.string.action_next_task else Res.string.action_check))
        }
    }
}
