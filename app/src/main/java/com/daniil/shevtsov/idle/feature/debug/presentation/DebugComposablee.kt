package com.daniil.shevtsov.idle.feature.debug.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.idle.feature.player.job.domain.Butcher
import com.daniil.shevtsov.idle.feature.player.job.domain.Mortician
import com.daniil.shevtsov.idle.feature.player.job.domain.PlayerJob

@Preview
@Composable
fun DebugComposablePreview() {
    DebugComposable(
        state = DebugViewState(
            jobSelection = listOf(
                PlayerJob(title = "LOL", tags = emptyList()),
                PlayerJob(title = "KEK", tags = emptyList()),
            )
        ),
        onAction = {},
    )
}

@Composable
fun DebugComposable(
    state: DebugViewState,
    onAction: (DebugViewAction) -> Unit,
) {
    var expanded: Boolean by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.TopStart)) {
        Text(
            text = if(state.jobSelection.isNotEmpty()) {state.jobSelection.first().title} else {"EMPTY"},
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded = true })
                .background(Color.Gray))
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            state.jobSelection.forEach { job ->
                DropdownMenuItem(
                    onClick = {
                        onAction(DebugViewAction.JobSelected(job))
                        expanded = false
                    }
                ) {
                    Text(text = job.title)
                }
            }
        }
    }
}
