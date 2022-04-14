package com.daniil.shevtsov.idle.feature.ending.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun EndinigPreview() {
    EndingComposable()
}

data class UnlockFeatureModel(
    val title: String,
    val subtitle: String,
)

data class UnlockModel(
    val title: String,
    val subtitle: String,
    val unlockFeatures: List<UnlockFeatureModel>,
)

@Composable
fun EndingComposable() {
    Column {
        Text("""You have been captured by the government.""")
        LazyColumn {
            items(listOf(
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
            )) { unlock ->
                Column {
                    Text(unlock.title)
                    Text(unlock.subtitle)
                    Column {
                        unlock.unlockFeatures.forEach { feature ->
                            Column {
                                Text(feature.title)
                                Text(feature.subtitle)
                            }
                        }
                    }
                }
            }
        }
        LazyColumn {
            items(listOf(
                "Max blood" to "1000",
                "Max money" to "20000",
                "Pets eaten" to "5",
                "People eaten" to "3",
            )) { (description, value) ->
                Row {
                    Text(description)
                    Text(value)
                }
            }
        }

    }
}