package com.daniil.shevtsov.idle.feature.player.info.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.idle.core.presentation.formatting.formatEnumName
import com.daniil.shevtsov.idle.core.ui.Pallete
import com.daniil.shevtsov.idle.core.ui.cavitary
import com.daniil.shevtsov.idle.feature.player.info.presentation.PlayerInfoState
import com.daniil.shevtsov.idle.feature.player.job.domain.playerJob
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId
import com.daniil.shevtsov.idle.feature.player.trait.domain.playerTrait
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tag

@Preview
@Composable
fun PlayerInfoComposablePreview() {
    PlayerInfoComposable(
        state = PlayerInfoState(
            playerJob = playerJob(
                title = "Memelogist",
                tags = listOf(
                    tag(name = "Knowledge of memes"),
                    tag(name = "Hoard of meme folders")
                )
            ),
            playerTraits = listOf(
                playerTrait(
                    title = "Memelogist",
                    traitId = TraitId.Job,
                    tags = listOf(
                        tag(name = "Knowledge of memes"),
                        tag(name = "Hoard of meme folders")
                    ),
                ),
                playerTrait(
                    title = "Neckbeard",
                    traitId = TraitId.Species,
                    tags = listOf(
                        tag(name = "Neckbeard"),
                        tag(name = "Fedora"),
                        tag(name = "Nice Guy"),
                    ),
                ),
            ),
            playerTags = listOf(
                tag(name = "Antisocial"),
                tag(name = "Basement Dweller"),
            )
        )
    )
}

@Composable
fun PlayerInfoComposable(
    state: PlayerInfoState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(Pallete.Red)
            .fillMaxSize(),
        verticalArrangement = spacedBy(16.dp)
    ) {
        Column(
            verticalArrangement = spacedBy(4.dp),
        ) {
            state.playerTraits.forEach { trait ->
                Row {
                    Text(
                        text = formatEnumName(trait.traitId.name),
                        fontSize = 24.sp,
                        color = Color.White,
                        modifier = Modifier
                            .background(Pallete.Red)
                            .weight(1f)
                    )
                    Text(
                        text = trait.title,
                        fontSize = 16.sp,
                        modifier = modifier
                            .cavitary(
                                lightColor = Pallete.LightRed,
                                darkColor = Pallete.DarkRed
                            )
                            .background(Color.White)
                            .padding(4.dp)
                            .weight(1f)
                    )
                }
            }
        }
        Column(verticalArrangement = spacedBy(8.dp)) {
            Text(
                text = "Tags",
                fontSize = 24.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(Pallete.Red)
                    .fillMaxWidth()
            )
            Text(
                text = state.playerTags.joinToString(separator = "\n") { tag ->
                    val traitId = state.playerTraits.find { trait ->
                        tag in trait.tags
                    }?.traitId
                    val type = traitId?.name?.let(::formatEnumName) ?: "General"
                    tag.name + " ($type)"
                },
                fontSize = 16.sp,
                modifier = modifier
                    .cavitary(
                        lightColor = Pallete.LightRed,
                        darkColor = Pallete.DarkRed
                    )
                    .background(Color.White)
                    .padding(4.dp)
                    .fillMaxWidth()
            )
        }
    }
}
