package de.vegnpunk.mentalmatics.ui.difficultyselection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.vegnpunk.mentalmatics.core.generation.Difficulty
import mentalmatics.ui.generated.resources.Res
import mentalmatics.ui.generated.resources.difficulty_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DifficultySelectionScreen(viewModel: DifficultySelectionViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    DifficultySelectionContent(uiState = uiState, onEvent = viewModel::onEvent)
}

@Composable
private fun DifficultySelectionContent(
    uiState: DifficultySelectionUiState,
    onEvent: (DifficultySelectionEvent) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .safeContentPadding()
                .fillMaxSize()
                .padding(16.dp)
                .selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(Res.string.difficulty_title),
            style = MaterialTheme.typography.titleLarge,
        )
        uiState.availableDifficulties.forEach { difficulty ->
            DifficultyRow(
                difficulty = difficulty,
                selected = difficulty == uiState.selectedDifficulty,
                onSelect = { onEvent(DifficultySelectionEvent.SelectDifficulty(difficulty)) },
            )
        }
    }
}

@Composable
private fun DifficultyRow(
    difficulty: Difficulty,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().selectable(selected = selected, onClick = onSelect),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = onSelect)
        Text(stringResource(difficulty.labelRes))
    }
}
