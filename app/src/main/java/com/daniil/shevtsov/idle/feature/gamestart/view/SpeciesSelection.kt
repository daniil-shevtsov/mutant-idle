package com.daniil.shevtsov.idle.feature.gamestart.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.idle.core.ui.Icons
import com.daniil.shevtsov.idle.core.ui.Pallete
import com.daniil.shevtsov.idle.core.ui.cavitary
import com.daniil.shevtsov.idle.feature.gamestart.presentation.TraitSelectionItem
import com.daniil.shevtsov.idle.feature.gamestart.presentation.traitSelectionItem

@Preview(
    widthDp = 650,
    heightDp = 600
)
@Composable
fun SpeciesSelectionPreview() {
    Column {
        selectedStatesOfSpecies().forEach { selection ->
            SpeciesSelection(
                selection = selection,
                onItemClicked = {},
            )
        }
    }
}

@Composable
fun SpeciesSelection(
    selection: List<TraitSelectionItem>,
    onItemClicked: (id: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(Pallete.Red)
            .padding(4.dp)
            .cavitary(
                lightColor = Pallete.LightRed,
                darkColor = Pallete.DarkRed
            )
            .background(Pallete.DarkGray)
            .padding(4.dp)
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            modifier = modifier
                .background(Pallete.Red)
                .padding(bottom = 4.dp)
                .background(Pallete.DarkGray)
                .padding(top = 4.dp)
                .height(IntrinsicSize.Max)
                .fillMaxWidth(),
        ) {
            selection.forEachIndexed { index, item ->
                Column(
                    horizontalAlignment = CenterHorizontally,
                    modifier = if (item.isSelected) {
                        modifier
                    } else {
                        modifier
                            .padding(bottom = 2.dp)
                    }
                        .clickable { onItemClicked(item.id) }
                        .width(IntrinsicSize.Min)
                ) {
                    Box(
                        modifier = modifier
                            .size(35.dp)
                            .let { modifier ->
                                if (item.isSelected) {
                                    modifier
                                        .background(Pallete.LightRed)
                                        .padding(start = 1.dp, top = 1.dp, end = 1.dp)
                                } else {
                                    modifier
                                }
                            }

                            .background(Pallete.Red),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (item.isUnlocked) {
                                true -> item.icon ?: Icons.TraitDefault
                                else -> Icons.LockedCharacter
                            },
                            fontSize = 24.sp
                        )
                    }
                    Row(
                        modifier = modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .let { modifier ->
                                if (item.isSelected) {
                                    modifier.background(Pallete.LightRed)
                                } else {
                                    modifier
                                }
                            },
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = modifier
                                .weight(1f)
                                .height(1.dp)
                                .let { modifier ->
                                    if (item.isSelected) {
                                        modifier
                                    } else {
                                        modifier
                                            .background(Pallete.Red)
                                    }
                                }
                        )
                        Box(
                            modifier = modifier
                                .width(35.dp)
                                .height(1.dp)
                                .let { modifier ->
                                    if (item.isSelected) {
                                        modifier.padding(horizontal = 1.dp)
                                    } else {
                                        modifier
                                    }
                                }
                                .background(Pallete.Red)
                        )
                        Box(
                            modifier = modifier
                                .weight(1f)
                                .height(1.dp)
                                .let { modifier ->
                                    if (item.isSelected) {
                                        modifier
                                    } else {
                                        modifier
                                            .background(Pallete.Red)
                                    }
                                }
                        )
                    }
                    Column(
                        modifier = modifier
                            .let { modifier ->
                                if (item.isSelected) {
                                    modifier
                                        .background(Pallete.LightRed)
                                        .let { kek ->
                                            when (index) {
                                                0 -> kek.padding(end = 1.dp)
                                                selection.size - 1 -> kek.padding(
                                                    start = 1.dp,
                                                )
                                                else -> kek.padding(
                                                    start = 1.dp,
                                                    end = 1.dp
                                                )
                                            }
                                        }
                                        .background(Pallete.Red)
                                } else {
                                    modifier
                                }
                            }
                            .background(Pallete.Red)
                            .padding(4.dp),
                    ) {
                        Text(
                            modifier = modifier.fillMaxWidth(),
                            text = when (item.isUnlocked) {
                                true -> item.title
                                else -> "Locked"
                            },
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontSize = 18.sp,
                        )
                        Text(
                            modifier = modifier
                                .fillMaxHeight()
                                .padding(4.dp)
                                .cavitary(
                                    lightColor = Pallete.LightRed,
                                    darkColor = Pallete.DarkRed
                                )
                                .background(Color.White)
                                .padding(4.dp)
                                .width(110.dp),
                            text = when (item.isUnlocked) {
                                true -> item.description
                                else -> "Do X to unlock this species"
                            },
                            fontSize = 14.sp,
                        )
                    }
                }

            }
        }
    }
}

fun selectedStatesOfSpecies() = (0..2)
    .mapIndexed { index, selectedIndex ->
        speciesSelectionComposeStub(selectedIndex = selectedIndex)
    }

fun speciesSelectionComposeStub(selectedIndex: Int = 1) = listOf(
    traitSelectionItem(
        title = "Devourer",
        icon = Icons.Devourer,
        description = "You are a growing creature with insatiable hunger",
        isSelected = selectedIndex == 0,
    ),
    traitSelectionItem(
        title = "Vampire",
        icon = Icons.Vampire,
        description = "You are a bloodsucking immortal creature",
        isSelected = selectedIndex == 1,
    ),
    traitSelectionItem(
        title = "Alien",
        icon = Icons.Alien,
        description = "You have crashed on this planet and need to find a way home",
        isSelected = selectedIndex == 2,
    ),
    traitSelectionItem(
        title = "Lol",
        icon = Icons.Alien,
        description = "Kek",
        isSelected = false,
        isUnlocked = false,
    ),
)
