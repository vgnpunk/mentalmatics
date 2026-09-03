package de.vegnpunk.mentalmatics.ui.session

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticOperation
import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticSessionReport
import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticTaskResult
import de.vegnpunk.mentalmatics.core.generation.DigitCount
import de.vegnpunk.mentalmatics.core.generation.SessionFeedbackMode
import de.vegnpunk.mentalmatics.core.generation.SessionLength
import mentalmatics.ui.generated.resources.Res
import mentalmatics.ui.generated.resources.action_back_to_setup
import mentalmatics.ui.generated.resources.report_correct_answer
import mentalmatics.ui.generated.resources.report_score
import mentalmatics.ui.generated.resources.report_title
import mentalmatics.ui.generated.resources.report_your_answer
import mentalmatics.ui.generated.resources.session_incorrect_try_again
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SessionScreen(
    selectedOperations: Set<ArithmeticOperation>,
    digitCount: DigitCount,
    sessionLength: SessionLength,
    feedbackMode: SessionFeedbackMode,
    onFinished: () -> Unit,
    viewModel: SessionViewModel =
        koinViewModel {
            parametersOf(selectedOperations, digitCount, sessionLength, feedbackMode)
        },
) {
    val uiState by viewModel.uiState.collectAsState()
    when (val state = uiState) {
        is SessionUiState.InProgress -> InProgressContent(state = state, onEvent = viewModel::onEvent)
        is SessionUiState.Completed -> ReportContent(report = state.report, onFinished = onFinished)
    }
}

@Composable
private fun InProgressContent(
    state: SessionUiState.InProgress,
    onEvent: (SessionEvent) -> Unit,
) {
    Column(
        modifier = Modifier.safeContentPadding().fillMaxSize().padding(16.dp),
    ) {
        // Fixed 50/50 split (not content-sized) so the keypad's position never
        // shifts when the retry-feedback text above appears or disappears, and
        // sits within comfortable one-handed thumb reach on the lower half.
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(progressLabel(state), style = MaterialTheme.typography.labelLarge)
            Text(state.task.toDisplayString(), style = MaterialTheme.typography.headlineMedium)
            Text(state.answerInput.ifEmpty { "_" }, style = MaterialTheme.typography.headlineSmall)
            if (state.lastAttemptIncorrect) {
                Text(
                    text = stringResource(Res.string.session_incorrect_try_again),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            NumericKeypad(
                onDigit = { onEvent(SessionEvent.DigitPressed(it)) },
                onBackspace = { onEvent(SessionEvent.BackspacePressed) },
                onConfirm = { onEvent(SessionEvent.ConfirmPressed) },
                confirmEnabled = state.answerInput.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

private fun progressLabel(state: SessionUiState.InProgress): String =
    when (val length = state.sessionLength) {
        is SessionLength.TaskCount -> "${state.completedCount + 1} / ${length.count}"
        is SessionLength.Duration -> formatRemaining(length.totalSeconds - state.elapsedSeconds)
    }

private fun formatRemaining(remainingSeconds: Int): String {
    val clamped = remainingSeconds.coerceAtLeast(0)
    val minutes = clamped / 60
    val seconds = clamped % 60
    return "$minutes:${seconds.toString().padStart(2, '0')}"
}

@Composable
private fun ReportContent(
    report: ArithmeticSessionReport,
    onFinished: () -> Unit,
) {
    Column(
        modifier = Modifier.safeContentPadding().fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(stringResource(Res.string.report_title), style = MaterialTheme.typography.titleLarge)
        Text(
            stringResource(Res.string.report_score, report.correctCount, report.totalCount),
            style = MaterialTheme.typography.headlineSmall,
        )
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(report.results) { result ->
                ReportResultRow(result)
                HorizontalDivider()
            }
        }
        Button(onClick = onFinished, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(Res.string.action_back_to_setup))
        }
    }
}

@Composable
private fun ReportResultRow(result: ArithmeticTaskResult) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = if (result.isCorrect) Icons.Filled.CheckCircle else Icons.Filled.Cancel,
            contentDescription = null,
            tint = if (result.isCorrect) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error,
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("${result.task.toDisplayString()} ${result.task.result}")
            if (!result.isCorrect) {
                Text(
                    text = stringResource(Res.string.report_your_answer, result.userAnswer?.toString() ?: "—"),
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(
                    text = stringResource(Res.string.report_correct_answer, result.task.result.toString()),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}
