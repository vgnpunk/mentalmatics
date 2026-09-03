package de.vegnpunk.mentalmatics.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import mentalmatics.ui.generated.resources.Res
import mentalmatics.ui.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier.safeContentPadding().fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(stringResource(Res.string.app_name))
    }
}
