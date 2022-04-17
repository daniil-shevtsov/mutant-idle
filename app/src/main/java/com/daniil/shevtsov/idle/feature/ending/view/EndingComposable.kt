package com.daniil.shevtsov.idle.feature.ending.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
    Column(modifier = Modifier.background(Pallete.Red)) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
            text = "You have been captured by the government.",
            fontSize = 24.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
        )
        Cavity(mainColor = Pallete.Red, modifier = Modifier.fillMaxWidth()) {
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)) {
                items(
                    listOf(
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
                ) { unlock ->
                    Column(modifier = Modifier
                        .background(Pallete.Red)
                        .padding(4.dp)) {
                        Text(
                            text = unlock.title,
                            color = Color.White,
                            fontSize = 24.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Pallete.DarkRed)
                                .padding(4.dp)
                        )
                        Text(
                            text = unlock.subtitle,
                            color = Color.White,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Pallete.DarkRed)
                                .padding(4.dp)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
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
//        LazyColumn {
//            items(
//                listOf(
//                    "Max blood" to "1000",
//                    "Max money" to "20000",
//                    "Pets eaten" to "5",
//                    "People eaten" to "3",
//                )
//            ) { (description, value) ->
//                Row {
//                    Text(description)
//                    Text(value)
//                }
//            }
//        }

    }
}