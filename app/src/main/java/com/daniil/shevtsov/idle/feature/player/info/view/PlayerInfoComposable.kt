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
import com.daniil.shevtsov.idle.core.ui.Pallete
import com.daniil.shevtsov.idle.core.ui.cavitary
import com.daniil.shevtsov.idle.feature.player.info.presentation.PlayerInfoState
import com.daniil.shevtsov.idle.feature.player.job.domain.playerJob
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
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = spacedBy(16.dp)
    ) {
        Row {
            Text(
                text = "Job",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier
                    .background(Pallete.Red)
                    .weight(1f)
            )
            Text(
                text = state.playerJob.title,
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
                text = state.playerJob.tags.joinToString(separator = "\n") { tag -> tag.name },
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
