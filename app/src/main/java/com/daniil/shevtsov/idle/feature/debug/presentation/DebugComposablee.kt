package com.daniil.shevtsov.idle.feature.debug.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.daniil.shevtsov.idle.core.ui.Pallete
import com.daniil.shevtsov.idle.core.ui.cavitary
import com.daniil.shevtsov.idle.core.ui.debugViewState
import com.daniil.shevtsov.idle.feature.player.job.domain.playerJobModel
import com.daniil.shevtsov.idle.feature.player.species.domain.playerSpeciesModel

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
            .background(Pallete.Red)
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        Column {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Pallete.Red),
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
                                lightColor = Pallete.LightRed,
                                darkColor = Pallete.DarkRed
                            )
                            .background(Color.White)
                            .clickable(onClick = { expanded1 = true })
                            .padding(4.dp)
                    )
                    DropdownMenu(
                        expanded = expanded1,
                        modifier = modifier,
                        onDismissRequest = { expanded1 = false }) {
                        state.jobSelection.forEach { job ->
                            DropdownMenuItem(
                                modifier = modifier.background(Color.White),
                                onClick = {
                                    onAction(DebugViewAction.JobSelected(job.id))
                                    expanded1 = false
                                }
                            ) {
                                Text(text = job.title)
                            }
                        }
                    }
                }
            }
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Pallete.Red),
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
                                lightColor = Pallete.LightRed,
                                darkColor = Pallete.DarkRed
                            )
                            .background(Color.White)
                            .clickable(onClick = { expanded2 = true })
                            .padding(4.dp)
                    )
                    DropdownMenu(
                        expanded = expanded2,
                        modifier = modifier.wrapContentHeight(),
                        onDismissRequest = { expanded2 = false }) {
                        state.speciesSelection.forEach { species ->
                            DropdownMenuItem(
                                modifier = modifier.background(Color.White),
                                onClick = {
                                    onAction(DebugViewAction.SpeciesSelected(species.id))
                                    expanded2 = false
                                }
                            ) {
                                Text(text = species.title)
                            }
                        }
                    }
                }
            }
        }
    }

//    Box(
//        modifier = Modifier
//            .background(Pallete.Red)
//            .fillMaxSize()
//            .wrapContentSize(Alignment.TopStart)
//    ) {
//        Column {
//            Row(
//                modifier = modifier
//                    .fillMaxWidth()
//                    .background(Pallete.Red),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(10.dp)
//            ) {
//                Text(
//                    modifier = modifier,
//                    text = "Selected Job:",
//                    fontSize = 24.sp,
//                    color = Color.White
//                )
//                Box {
//                    Text(
//                        text = when (val selectedJob =
//                            state.jobSelection.firstOrNull { it.isSelected }
//                                ?: state.jobSelection.firstOrNull()) {
//                            null -> "EMPTY"
//                            else -> selectedJob.title
//                        },
//                        fontSize = 16.sp,
//                        modifier = modifier
//                            .fillMaxWidth()
//                            .cavitary(
//                                lightColor = Pallete.LightRed,
//                                darkColor = Pallete.DarkRed
//                            )
//                            .background(Color.White)
//                            .clickable(onClick = { expanded = true })
//                            .padding(4.dp)
//                    )
//                    DropdownMenu(
//                        expanded = expanded,
//                        modifier = modifier,
//                        onDismissRequest = { expanded = false }) {
//                        state.jobSelection.forEach { job ->
//                            DropdownMenuItem(
//                                onClick = {
//                                    onAction(DebugViewAction.JobSelected(job.id))
//                                    expanded = false
//                                }
//                            ) {
//                                Text(text = job.title)
//                            }
//                        }
//                    }
//                }
//            }

}
