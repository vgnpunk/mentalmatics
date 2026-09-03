package de.vegnpunk.mentalmatics.ui.setup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticOperation
import de.vegnpunk.mentalmatics.core.generation.SessionFeedbackMode
import de.vegnpunk.mentalmatics.ui.navigation.Route
import de.vegnpunk.mentalmatics.ui.navigation.SessionLengthType
import mentalmatics.ui.generated.resources.Res
import mentalmatics.ui.generated.resources.action_start
import mentalmatics.ui.generated.resources.digit_count_title
import mentalmatics.ui.generated.resources.duration_minutes_suffix
import mentalmatics.ui.generated.resources.exercise_area_general_mental_math
import mentalmatics.ui.generated.resources.feedback_mode_report_at_end
import mentalmatics.ui.generated.resources.feedback_mode_retry_until_correct
import mentalmatics.ui.generated.resources.feedback_mode_title
import mentalmatics.ui.generated.resources.session_length_duration
import mentalmatics.ui.generated.resources.session_length_task_count
import mentalmatics.ui.generated.resources.session_length_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SetupScreen(
    onNavigate: (Route) -> Unit,
    viewModel: SetupViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.navigationEffect.collect { route -> onNavigate(route) }
    }

    SetupContent(uiState = uiState, onEvent = viewModel::onEvent)
}

@Composable
private fun SetupContent(
    uiState: SetupUiState,
    onEvent: (SetupEvent) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .safeContentPadding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = stringResource(Res.string.exercise_area_general_mental_math),
            style = MaterialTheme.typography.titleLarge,
        )

        OperationChips(
            operations = uiState.availableOperations,
            selectedOperations = uiState.selectedOperations,
            onToggle = { onEvent(SetupEvent.ToggleOperation(it)) },
        )

        DigitCountSelector(
            selectedDigitCount = uiState.digitCount,
            onSelect = { onEvent(SetupEvent.SelectDigitCount(it)) },
        )

        SessionLengthSelector(uiState = uiState, onEvent = onEvent)

        FeedbackModeSelector(
            selectedMode = uiState.feedbackMode,
            onSelect = { onEvent(SetupEvent.SelectFeedbackMode(it)) },
        )

        Button(
            onClick = { onEvent(SetupEvent.Start) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
        ) {
            Text(stringResource(Res.string.action_start), style = MaterialTheme.typography.titleMedium)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun OperationChips(
    operations: List<ArithmeticOperation>,
    selectedOperations: Set<ArithmeticOperation>,
    onToggle: (ArithmeticOperation) -> Unit,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        operations.forEach { operation ->
            FilterChip(
                selected = operation in selectedOperations,
                onClick = { onToggle(operation) },
                label = { Text(stringResource(operation.labelRes)) },
            )
        }
    }
}

@Composable
private fun FeedbackModeSelector(
    selectedMode: SessionFeedbackMode,
    onSelect: (SessionFeedbackMode) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(stringResource(Res.string.feedback_mode_title), style = MaterialTheme.typography.titleMedium)
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SessionFeedbackMode.entries.forEachIndexed { index, mode ->
                SegmentedButton(
                    selected = mode == selectedMode,
                    onClick = { onSelect(mode) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = SessionFeedbackMode.entries.size),
                ) {
                    Text(
                        stringResource(
                            if (mode == SessionFeedbackMode.REPORT_AT_END) {
                                Res.string.feedback_mode_report_at_end
                            } else {
                                Res.string.feedback_mode_retry_until_correct
                            },
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun DigitCountSelector(
    selectedDigitCount: Int,
    onSelect: (Int) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(stringResource(Res.string.digit_count_title), style = MaterialTheme.typography.titleMedium)
        val options = SetupUiState.DIGIT_COUNT_OPTIONS.toList()
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            options.forEachIndexed { index, digits ->
                SegmentedButton(
                    selected = digits == selectedDigitCount,
                    onClick = { onSelect(digits) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                ) {
                    Text(digits.toString())
                }
            }
        }
    }
}

@Composable
private fun SessionLengthSelector(
    uiState: SetupUiState,
    onEvent: (SetupEvent) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(stringResource(Res.string.session_length_title), style = MaterialTheme.typography.titleMedium)

        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SessionLengthType.entries.forEachIndexed { index, type ->
                SegmentedButton(
                    selected = type == uiState.sessionLengthType,
                    onClick = { onEvent(SetupEvent.SelectSessionLengthType(type)) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = SessionLengthType.entries.size),
                ) {
                    Text(
                        stringResource(
                            if (type == SessionLengthType.TASK_COUNT) {
                                Res.string.session_length_task_count
                            } else {
                                Res.string.session_length_duration
                            },
                        ),
                    )
                }
            }
        }

        when (uiState.sessionLengthType) {
            SessionLengthType.TASK_COUNT ->
                ValueChips(
                    values = SetupUiState.TASK_COUNT_OPTIONS,
                    selectedValue = uiState.taskCount,
                    labelFor = { it.toString() },
                    onSelect = { onEvent(SetupEvent.SelectTaskCount(it)) },
                )

            SessionLengthType.DURATION -> {
                val suffix = stringResource(Res.string.duration_minutes_suffix)
                ValueChips(
                    values = SetupUiState.DURATION_MINUTES_OPTIONS,
                    selectedValue = uiState.durationMinutes,
                    labelFor = { "$it $suffix" },
                    onSelect = { onEvent(SetupEvent.SelectDurationMinutes(it)) },
                )
            }
        }
    }
}

@Composable
private fun ValueChips(
    values: List<Int>,
    selectedValue: Int,
    labelFor: (Int) -> String,
    onSelect: (Int) -> Unit,
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(values) { value ->
            FilterChip(
                modifier = Modifier.wrapContentWidth(),
                selected = value == selectedValue,
                onClick = { onSelect(value) },
                label = { Text(labelFor(value)) },
            )
        }
    }
}
