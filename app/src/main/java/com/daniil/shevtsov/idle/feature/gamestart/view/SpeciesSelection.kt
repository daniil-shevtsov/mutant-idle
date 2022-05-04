package com.daniil.shevtsov.idle.feature.gamestart.view

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.ui.Pallete
import com.daniil.shevtsov.idle.core.ui.cavitary
import com.daniil.shevtsov.idle.feature.gamestart.presentation.SpeciesSelectionItem
import com.daniil.shevtsov.idle.feature.gamestart.presentation.speciesSelectionItem

@Preview(widthDp = 230)
@Composable
fun SpeciesSelectionPreview() {
    SpeciesSelection(
        selection = speciesSelectionComposeStub()
    )
}

@Composable
fun SpeciesSelection(
    selection: List<SpeciesSelectionItem>,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.horizontalScroll(rememberScrollState())) {
        Row(
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
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        ) {
            selection.forEach { speciesItem ->
                Column(
                    modifier = modifier
                        .background(Pallete.LightRed)
                        .padding(start = 1.dp, top = 1.dp)
                        .background(Pallete.Red)
                        .width(200.dp)
                ) {
                    Text(
                        modifier = modifier.fillMaxWidth(),
                        text = speciesItem.title,
                        textAlign = TextAlign.Center,
                        color = Color.White,
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
                    )
                }
            }
        }
    }
}

fun speciesSelectionComposeStub() = listOf(
    speciesSelectionItem(
        title = "Devourer",
        description = "You are a growing creature with insatiable hunger",
    ),
    speciesSelectionItem(
        title = "Vampire",
        description = "You are a bloodsucking immortal creature",
    ),
)
