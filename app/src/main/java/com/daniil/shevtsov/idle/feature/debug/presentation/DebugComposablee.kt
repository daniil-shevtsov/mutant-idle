package com.daniil.shevtsov.idle.feature.debug.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        modifier = modifier
            .background(AppTheme.colors.background)
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        Column(verticalArrangement = spacedBy(AppTheme.dimensions.paddingSmall)) {
            SpeciesRow(
                state = state,
                expanded2 = expanded2,
                onExpandChange = { expanded2 = it },
                onAction = onAction,
            )
            JobRow(
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
    state: DebugViewState,
    expanded2: Boolean,
    modifier: Modifier = Modifier,
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
            style = AppTheme.typography.title,
            color = AppTheme.colors.textLight
        )
        Box(modifier = modifier.weight(1f)) {
            Text(
                text = when (val selectedSpecies =
                    state.speciesSelection.firstOrNull { it.isSelected }
                        ?: state.speciesSelection.firstOrNull()) {
                    null -> "EMPTY"
                    else -> selectedSpecies.title
                },
                style = AppTheme.typography.body,
                color = AppTheme.colors.textDark,
                modifier = modifier
                    .fillMaxWidth()
                    .cavitary(
                        lightColor = AppTheme.colors.backgroundLight,
                        darkColor = AppTheme.colors.backgroundDark
                    )
                    .background(AppTheme.colors.backgroundText)
                    .clickable(onClick = { onExpandChange(true) })
                    .padding(AppTheme.dimensions.paddingSmall)
            )
            DropdownMenu(
                expanded = expanded2,
                modifier = modifier.wrapContentHeight(),
                onDismissRequest = { onExpandChange(false) }) {
                state.speciesSelection.forEach { species ->
                    DropdownMenuItem(
                        modifier = modifier.background(AppTheme.colors.backgroundText),
                        onClick = {
                            onAction(DebugViewAction.TraitSelected(TraitId.Species, species.id))
                            onExpandChange(false)
                        }
                    ) {
                        Text(
                            text = species.title,
                            style = AppTheme.typography.body,
                            color = AppTheme.colors.textDark,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun JobRow(
    state: DebugViewState,
    expanded1: Boolean,
    modifier: Modifier = Modifier,
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
            style = AppTheme.typography.title,
            color = AppTheme.colors.textLight
        )
        Box(modifier = modifier.weight(1f)) {
            Text(
                text = when (val selectedJob =
                    state.jobSelection.firstOrNull { it.isSelected }
                        ?: state.jobSelection.firstOrNull()) {
                    null -> "EMPTY"
                    else -> selectedJob.title
                },
                style = AppTheme.typography.body,
                color = AppTheme.colors.textDark,
                modifier = modifier
                    .fillMaxWidth()
                    .cavitary(
                        lightColor = AppTheme.colors.backgroundLight,
                        darkColor = AppTheme.colors.backgroundDark
                    )
                    .background(AppTheme.colors.backgroundText)
                    .clickable(onClick = { onExpandChange(true) })
                    .padding(AppTheme.dimensions.paddingSmall)
            )
            DropdownMenu(
                expanded = expanded1,
                modifier = modifier,
                onDismissRequest = { onExpandChange(false) }) {
                state.jobSelection.forEach { job ->
                    DropdownMenuItem(
                        modifier = modifier.background(AppTheme.colors.backgroundText),
                        onClick = {
                            onAction(DebugViewAction.TraitSelected(TraitId.Job, job.id))
                            onExpandChange(false)
                        }
                    ) {
                        Text(
                            text = job.title,
                            style = AppTheme.typography.body,
                            color = AppTheme.colors.textDark,
                        )
                    }
                }
            }
        }
    }
}
