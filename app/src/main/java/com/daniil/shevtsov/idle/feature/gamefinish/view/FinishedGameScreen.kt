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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme
import com.daniil.shevtsov.idle.core.ui.widgets.Cavity
import com.daniil.shevtsov.idle.core.ui.widgets.CavityButton
import com.daniil.shevtsov.idle.feature.gamefinish.presentation.FinishedGameViewState
import com.daniil.shevtsov.idle.feature.gamefinish.presentation.UnlockFeatureModel
import com.daniil.shevtsov.idle.feature.gamefinish.presentation.UnlockModel
import com.daniil.shevtsov.idle.feature.gamefinish.presentation.endingViewState
import com.daniil.shevtsov.idle.feature.gamefinish.presentation.previewFinishedGameViewState

@Preview
@Composable
fun FinishedGameScreenPreview() {
    FinishedGameScreen(
        state = previewFinishedGameViewState(
            endingState = endingViewState(
                title = "You have been captured by the government",
                description = "They won't let you see sunlight again"
            ),
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
    Column(
        verticalArrangement = spacedBy(AppTheme.dimensions.paddingMedium),
        modifier = Modifier
            .background(AppTheme.colors.background)
            .padding(horizontal = AppTheme.dimensions.paddingM)
            .padding(top = AppTheme.dimensions.paddingMedium),
    ) {
        Column(
            verticalArrangement = spacedBy(AppTheme.dimensions.paddingSmall),
            modifier = Modifier
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = state.endingState.title,
                style = AppTheme.typography.headerS,
                color = AppTheme.colors.textLight,
                textAlign = TextAlign.Center,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = state.endingState.description,
                style = AppTheme.typography.title,
                color = AppTheme.colors.textLight,
                textAlign = TextAlign.Center,
            )
        }

        Cavity(mainColor = AppTheme.colors.background, modifier = Modifier.fillMaxWidth()) {
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
                            .background(AppTheme.colors.background)
                            .padding(6.dp)
                    ) {
                        Text(
                            text = unlock.title,
                            style = AppTheme.typography.title,
                            color = AppTheme.colors.textLight,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(AppTheme.colors.backgroundDark)
                                .padding(8.dp)
                        )
                        Text(
                            text = unlock.subtitle,
                            color = AppTheme.colors.textLight,
                            style = AppTheme.typography.subtitle,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(AppTheme.colors.backgroundDark)
                                .padding(8.dp)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(AppTheme.colors.backgroundText)
                                .padding(AppTheme.dimensions.paddingSmall)
                        ) {
                            unlock.unlockFeatures.forEach { feature ->
                                Column {
                                    Text(
                                        text = feature.title,
                                        style = AppTheme.typography.bodyTitle,
                                        color = AppTheme.colors.textDark,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(AppTheme.colors.backgroundText)
                                            .padding(AppTheme.dimensions.paddingSmall)
                                    )
                                    if (feature.subtitle.isNotEmpty()) {
                                        Text(
                                            text = feature.subtitle,
                                            style = AppTheme.typography.body,
                                            color = AppTheme.colors.textDark,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(AppTheme.colors.backgroundText)
                                                .padding(AppTheme.dimensions.paddingSmall)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        CavityButton(
            text = "Start new game",
            onClick = {},
            modifier = Modifier.padding(bottom = AppTheme.dimensions.paddingMedium)
        )
    }
}
