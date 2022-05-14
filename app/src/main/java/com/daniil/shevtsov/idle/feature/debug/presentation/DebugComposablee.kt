package com.daniil.shevtsov.idle.feature.debug.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.daniil.shevtsov.idle.core.ui.cavitary
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme
import com.daniil.shevtsov.idle.core.ui.widgets.CavityButton
import com.daniil.shevtsov.idle.feature.player.job.domain.playerJobModel
import com.daniil.shevtsov.idle.feature.player.species.domain.playerSpeciesModel
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId

@Preview(
    widthDp = 230,
    heightDp = 534,
)
@Composable
fun DebugComposablePreview() {
    DebugComposable(
        state = debugViewState(
            jobSelection = listOf(
                playerJobModel(title = "Mortician"),
                playerJobModel(title = "KEK", tags = emptyList()),
            ),
            speciesSelection = listOf(
                playerSpeciesModel(title = "Devourer")
            )
        ),
        onAction = {},
    )
}

@Composable
fun DebugComposable(
    state: DebugViewState,
    onAction: (DebugViewAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded1: Boolean by remember { mutableStateOf(false) }
    var expanded2: Boolean by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .background(AppTheme.colors.background)
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        Column(verticalArrangement = spacedBy(4.dp)) {
            SpeciesRow(
                modifier = modifier,
                state = state,
                expanded2 = expanded2,
                onExpandChange = { expanded2 = it },
                onAction = onAction,
            )
            JobRow(
                modifier = modifier,
                state = state,
                expanded1 = expanded1,
                onExpandChange = { expanded1 = it },
                onAction = onAction,
            )
            CavityButton(
                onClick = { onAction(DebugViewAction.UnlockEverything) },
                text = "Unlock everything",
            )
        }
    }
}

@Composable
private fun SpeciesRow(
    modifier: Modifier,
    state: DebugViewState,
    expanded2: Boolean,
    onExpandChange: (newValue: Boolean) -> Unit,
    onAction: (DebugViewAction) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.colors.background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            modifier = modifier.weight(1f),
            text = "Selected Species:",
            fontSize = 24.sp,
            color = Color.White
        )
        Box(modifier = modifier.weight(1f)) {
            Text(
                text = when (val selectedSpecies =
                    state.speciesSelection.firstOrNull { it.isSelected }
                        ?: state.speciesSelection.firstOrNull()) {
                    null -> "EMPTY"
                    else -> selectedSpecies.title
                },
                fontSize = 16.sp,
                modifier = modifier
                    .fillMaxWidth()
                    .cavitary(
                        lightColor = AppTheme.colors.backgroundLight,
                        darkColor = AppTheme.colors.backgroundDark
                    )
                    .background(Color.White)
                    .clickable(onClick = { onExpandChange(true) })
                    .padding(4.dp)
            )
            DropdownMenu(
                expanded = expanded2,
                modifier = modifier.wrapContentHeight(),
                onDismissRequest = { onExpandChange(false) }) {
                state.speciesSelection.forEach { species ->
                    DropdownMenuItem(
                        modifier = modifier.background(Color.White),
                        onClick = {
                            onAction(DebugViewAction.TraitSelected(TraitId.Species, species.id))
                            onExpandChange(false)
                        }
                    ) {
                        Text(text = species.title)
                    }
                }
            }
        }
    }
}

@Composable
private fun JobRow(
    modifier: Modifier,
    state: DebugViewState,
    expanded1: Boolean,
    onExpandChange: (newValue: Boolean) -> Unit,
    onAction: (DebugViewAction) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.colors.background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            modifier = modifier.weight(1f),
            text = "Selected Job:",
            fontSize = 24.sp,
            color = Color.White
        )
        Box(modifier = modifier.weight(1f)) {
            Text(
                text = when (val selectedJob =
                    state.jobSelection.firstOrNull { it.isSelected }
                        ?: state.jobSelection.firstOrNull()) {
                    null -> "EMPTY"
                    else -> selectedJob.title
                },
                fontSize = 16.sp,
                modifier = modifier
                    .fillMaxWidth()
                    .cavitary(
                        lightColor = AppTheme.colors.backgroundLight,
                        darkColor = AppTheme.colors.backgroundDark
                    )
                    .background(Color.White)
                    .clickable(onClick = { onExpandChange(true) })
                    .padding(4.dp)
            )
            DropdownMenu(
                expanded = expanded1,
                modifier = modifier,
                onDismissRequest = { onExpandChange(false) }) {
                state.jobSelection.forEach { job ->
                    DropdownMenuItem(
                        modifier = modifier.background(Color.White),
                        onClick = {
                            onAction(DebugViewAction.TraitSelected(TraitId.Job, job.id))
                            onExpandChange(false)
                        }
                    ) {
                        Text(text = job.title)
                    }
                }
            }
        }
    }
}
