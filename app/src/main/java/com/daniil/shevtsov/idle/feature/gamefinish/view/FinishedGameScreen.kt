package com.daniil.shevtsov.idle.feature.gamefinish.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme
import com.daniil.shevtsov.idle.core.ui.widgets.Cavity
import com.daniil.shevtsov.idle.core.ui.widgets.CavityButton
import com.daniil.shevtsov.idle.feature.gamefinish.presentation.FinishedGameViewState
import com.daniil.shevtsov.idle.feature.gamefinish.presentation.UnlockFeatureModel
import com.daniil.shevtsov.idle.feature.gamefinish.presentation.UnlockModel
import com.daniil.shevtsov.idle.feature.gamefinish.presentation.endingViewState
import com.daniil.shevtsov.idle.feature.gamefinish.presentation.previewFinishedGameViewState
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction

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
        ),
        onAction = {},
    )
}

@Composable
fun FinishedGameScreen(
    state: FinishedGameViewState,
    modifier: Modifier = Modifier,
    onAction: (action: MainViewAction) -> Unit,
) {
    Column(
        verticalArrangement = spacedBy(AppTheme.dimensions.paddingMedium),
        modifier = modifier
            .background(AppTheme.colors.background)
            .padding(horizontal = AppTheme.dimensions.paddingM)
            .padding(top = AppTheme.dimensions.paddingMedium)
            .fillMaxSize(),
    ) {
        Column(
            verticalArrangement = spacedBy(AppTheme.dimensions.paddingSmall),
            modifier = Modifier
        ) {
            Text(
                text = state.endingState.title,
                style = AppTheme.typography.headerS,
                color = AppTheme.colors.textLight,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = state.endingState.description,
                style = AppTheme.typography.title,
                color = AppTheme.colors.textLight,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Cavity(
            mainColor = AppTheme.colors.background,
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyColumn(
                contentPadding = PaddingValues(AppTheme.dimensions.paddingMedium),
                verticalArrangement = spacedBy(AppTheme.dimensions.paddingM),
            ) {
                items(state.unlocks) { unlock ->
                    Column {
                        Column(
                            modifier = Modifier
                                .background(AppTheme.colors.backgroundDark)
                                .padding(AppTheme.dimensions.paddingSmall)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = unlock.title,
                                style = AppTheme.typography.title,
                                color = AppTheme.colors.textLight,
                            )
                            Text(
                                text = unlock.subtitle,
                                color = AppTheme.colors.textLight,
                                style = AppTheme.typography.subtitle,
                            )
                        }

                        Column(
                            modifier = Modifier
                                .background(AppTheme.colors.backgroundText)
                                .padding(AppTheme.dimensions.paddingSmall)
                                .fillMaxWidth(),
                            verticalArrangement = spacedBy(AppTheme.dimensions.paddingSmall)
                        ) {
                            unlock.unlockFeatures.forEach { feature ->
                                Column {
                                    Text(
                                        text = feature.title,
                                        style = AppTheme.typography.bodyTitle,
                                        color = AppTheme.colors.textDark,
                                    )
                                    if (feature.subtitle.isNotEmpty()) {
                                        Text(
                                            text = feature.subtitle,
                                            style = AppTheme.typography.body,
                                            color = AppTheme.colors.textDark,
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
            onClick = { onAction(MainViewAction.StartNewGameClicked) },
            modifier = Modifier
                .padding(bottom = AppTheme.dimensions.paddingMedium)
                .fillMaxWidth()
        )
    }
}
