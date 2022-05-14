package com.daniil.shevtsov.idle.feature.gamefinish.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.idle.core.ui.Pallete
import com.daniil.shevtsov.idle.core.ui.widgets.Cavity
import com.daniil.shevtsov.idle.feature.gamefinish.presentation.*

@Preview
@Composable
fun FinishedGameScreenPreview() {
    FinishedGameScreen(
        state = finishedGameViewState(
            endingState = endingViewState(description = "You have been captured by the government"),
            unlocks = listOf(
                UnlockModel(
                    title = "New class: Vampire",
                    subtitle = "You are a bloodsucking immortal creature",
                    unlockFeatures = listOf(
                        UnlockFeatureModel(
                            title = "Can't be in the sun",
                            subtitle = "Reduced number of available human actions",
                        ),
                        UnlockFeatureModel(
                            title = "Hypnosis",
                            subtitle = "Can influence humans and gather familiars"
                        ),
                    )
                ),
                UnlockModel(
                    title = "New job: Mortician",
                    subtitle = "You work in a morgue",
                    unlockFeatures = listOf(
                        UnlockFeatureModel(
                            title = "Corpses access",
                            subtitle = "People bring them TO YOU",
                        ),
                        UnlockFeatureModel(
                            title = "Solitary job",
                            subtitle = "People don't pay attention as long as the job gets done"
                        ),
                    )
                ),
            )
        )
    )
}

@Composable
fun FinishedGameScreen(state: FinishedGameViewState) {
    Column(modifier = Modifier.background(Pallete.Background)) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
            text = state.endingState.description,
            fontSize = 24.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
        )
        Cavity(mainColor = Pallete.Background, modifier = Modifier.fillMaxWidth()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = spacedBy(12.dp),
            ) {
                items(state.unlocks) { unlock ->
                    Column(
                        modifier = Modifier
                            .background(Pallete.Background)
                            .padding(6.dp)
                    ) {
                        Text(
                            text = unlock.title,
                            color = Color.White,
                            fontSize = 24.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Pallete.BackgroundDark)
                                .padding(8.dp)
                        )
                        Text(
                            text = unlock.subtitle,
                            color = Color.White,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Pallete.BackgroundDark)
                                .padding(8.dp)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(4.dp)
                        ) {
                            unlock.unlockFeatures.forEach { feature ->
                                Column {
                                    Text(
                                        text = feature.title,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color.White)
                                            .padding(4.dp)
                                    )
                                    if(feature.subtitle.isNotEmpty()) {
                                        Text(
                                            text = feature.subtitle,
                                            fontSize = 16.sp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color.White)
                                                .padding(4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
