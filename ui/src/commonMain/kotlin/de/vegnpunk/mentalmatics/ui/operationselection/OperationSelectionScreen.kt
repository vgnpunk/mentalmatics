package de.vegnpunk.mentalmatics.ui.operationselection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.vegnpunk.mentalmatics.core.arithmetic.ArithmeticOperation
import de.vegnpunk.mentalmatics.ui.navigation.Route
import mentalmatics.ui.generated.resources.Res
import mentalmatics.ui.generated.resources.action_continue
import mentalmatics.ui.generated.resources.exercise_area_general_mental_math
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OperationSelectionScreen(
    onNavigate: (Route) -> Unit,
    viewModel: OperationSelectionViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.navigationEffect.collect { route -> onNavigate(route) }
    }

    OperationSelectionContent(uiState = uiState, onEvent = viewModel::onEvent)
}

@Composable
private fun OperationSelectionContent(
    uiState: OperationSelectionUiState,
    onEvent: (OperationSelectionEvent) -> Unit,
) {
    Column(
        modifier = Modifier.safeContentPadding().fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(Res.string.exercise_area_general_mental_math),
            style = MaterialTheme.typography.titleLarge,
        )
        uiState.availableOperations.forEach { operation ->
            OperationRow(
                operation = operation,
                checked = operation in uiState.selectedOperations,
                onCheckedChange = { onEvent(OperationSelectionEvent.ToggleOperation(operation)) },
            )
        }
        Button(onClick = { onEvent(OperationSelectionEvent.Continue) }) {
            Text(stringResource(Res.string.action_continue))
        }
    }
}

@Composable
private fun OperationRow(
    operation: ArithmeticOperation,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().toggleable(value = checked, onValueChange = onCheckedChange),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(stringResource(operation.labelRes))
    }
}
