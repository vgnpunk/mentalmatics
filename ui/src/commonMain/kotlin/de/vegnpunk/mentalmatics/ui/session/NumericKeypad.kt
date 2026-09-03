package de.vegnpunk.mentalmatics.ui.session

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val KEYPAD_SPACING = 8.dp
private const val KEYPAD_COLUMNS = 3
private const val KEYPAD_ROW_COUNT = 4

private val KEYPAD_ROWS =
    listOf(
        listOf(1, 2, 3),
        listOf(4, 5, 6),
        listOf(7, 8, 9),
    )

/**
 * On-screen number pad (US-5.3) so the system keyboard never has to
 * appear for answer entry.
 *
 * Button size is derived from both the available width and height
 * (via [BoxWithConstraints]) rather than just the width, so the grid
 * never overflows its container and rows can't overlap when the
 * container is shorter than 3 square, width-sized buttons per row
 * would need (see `docs/negative-knowledge.md`).
 */
@Composable
fun NumericKeypad(
    onDigit: (Int) -> Unit,
    onBackspace: () -> Unit,
    onConfirm: () -> Unit,
    confirmEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier) {
        val buttonSize =
            minOf(
                (maxWidth - KEYPAD_SPACING * (KEYPAD_COLUMNS - 1)) / KEYPAD_COLUMNS,
                (maxHeight - KEYPAD_SPACING * (KEYPAD_ROW_COUNT - 1)) / KEYPAD_ROW_COUNT,
            )
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(KEYPAD_SPACING),
        ) {
            KEYPAD_ROWS.forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(KEYPAD_SPACING)) {
                    row.forEach { digit ->
                        DigitKey(digit = digit, onClick = { onDigit(digit) }, size = buttonSize)
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(KEYPAD_SPACING)) {
                OutlinedButton(
                    onClick = onBackspace,
                    modifier = Modifier.size(buttonSize),
                    contentPadding = ButtonDefaults.TextButtonContentPadding,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Backspace,
                        contentDescription = null,
                        modifier = Modifier.size(buttonSize / 3),
                    )
                }
                DigitKey(digit = 0, onClick = { onDigit(0) }, size = buttonSize)
                Button(
                    onClick = onConfirm,
                    enabled = confirmEnabled,
                    modifier = Modifier.size(buttonSize),
                    contentPadding = ButtonDefaults.TextButtonContentPadding,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(buttonSize / 3),
                    )
                }
            }
        }
    }
}

@Composable
private fun DigitKey(
    digit: Int,
    onClick: () -> Unit,
    size: Dp,
) {
    FilledTonalButton(onClick = onClick, modifier = Modifier.size(size)) {
        Text(digit.toString(), style = MaterialTheme.typography.headlineMedium)
    }
}
