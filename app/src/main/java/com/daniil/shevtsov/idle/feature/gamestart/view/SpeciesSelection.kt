package com.daniil.shevtsov.idle.feature.gamestart.view

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            (selection + selection + selection + selection).forEach { speciesItem ->
                Column(
                    modifier = modifier
                        .width(100.dp)
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
