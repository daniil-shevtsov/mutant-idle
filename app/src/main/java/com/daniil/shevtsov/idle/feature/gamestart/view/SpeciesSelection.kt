package com.daniil.shevtsov.idle.feature.gamestart.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Bottom
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
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
import com.daniil.shevtsov.idle.feature.gamestart.presentation.SpeciesSelectionItem
import com.daniil.shevtsov.idle.feature.gamestart.presentation.speciesSelectionItem

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
    selection: List<SpeciesSelectionItem>,
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
            .horizontalScroll(rememberScrollState())
    ) {
        Row(
            modifier = modifier
                .background(Pallete.Red)
                .padding(bottom = 4.dp)
                .background(Pallete.DarkGray)
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        ) {
            selection.forEachIndexed { index, speciesItem ->
                Column(
                    horizontalAlignment = CenterHorizontally,
                    modifier = modifier
                        .let { modifier ->
                            if (speciesItem.isSelected) {
                                modifier.background(Pallete.Red)
                            } else {
                                modifier
                            }
                                .padding(bottom = 8.dp)
                                .background(Pallete.DarkGray)
                        }

                ) {
                    Box(
                        modifier = modifier
                            .size(35.dp)
//                            .background(Pallete.LightRed)
//                            .padding(start = 1.dp, top = 1.dp, end = 1.dp)
                            .background(Pallete.Red),
                        contentAlignment = Center
                    ) {
                        Text(text = speciesItem.icon, fontSize = 24.sp)
                    }
                    Row(
                        modifier = modifier
                            .height(1.dp)
                            .width(200.dp),
//                            .background(Pallete.LightRed),
                        horizontalArrangement = SpaceBetween
                    ) {
                        Box(
                            modifier = modifier
                                .height(1.dp)
                        )
                        Box(
                            modifier = modifier
                                .height(1.dp)
                                .width(35.dp)
                                .padding(horizontal = 1.dp)
                                .background(Pallete.Red)
                        )
                        Box(
                            modifier = modifier
                                .height(1.dp)
                                .let { modifier ->
                                    if (speciesItem.isSelected) {
                                        modifier
//                                            .background(Pallete.LightRed)
//                                            .padding(start = 1.dp)
//                                            .background(Pallete.Red)
                                    } else {
                                        modifier
                                    }
                                }
                        )
                    }
                    Column(
                        modifier = modifier
//                            .background(Pallete.LightRed)
//                            .padding(start = 1.dp)
                            .background(Color.Blue)
                            .width(200.dp)
                            .clickable { onItemClicked(speciesItem.id) },
                        verticalArrangement = Bottom,
                    ) {
                        Text(
                            modifier = modifier.fillMaxWidth(),
                            text = speciesItem.title,
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
                                .padding(4.dp),
                            text = speciesItem.description,
                            fontSize = 14.sp,
                        )
                    }
                }
            }
        }
    }
}

fun selectedStatesOfSpecies() = (1..1)
    .mapIndexed { index, selectedIndex ->
        speciesSelectionComposeStub(selectedIndex = selectedIndex)
    }

fun speciesSelectionComposeStub(selectedIndex: Int = 1) = listOf(
    speciesSelectionItem(
        title = "Devourer",
        icon = Icons.Devourer,
        description = "You are a growing creature with insatiable hunger",
        isSelected = selectedIndex == 0,
    ),
    speciesSelectionItem(
        title = "Vampire",
        icon = Icons.Vampire,
        description = "You are a bloodsucking immortal creature",
        isSelected = selectedIndex == 1,
    ),
    speciesSelectionItem(
        title = "Alien",
        icon = Icons.Alien,
        description = "You have crashed on this planet and need to find a way home",
        isSelected = selectedIndex == 2,
    )
)
