package com.daniil.shevtsov.idle.feature.player.info.view

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
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
    state: PlayerInfoState
) {
    val text =
        state.playerJob.title + ":\n" + state.playerJob.tags.joinToString(separator = "\n") { tag -> tag.name } + "\n\n\n" + state.playerTags
            .joinToString(separator = "\n") { tag -> tag.name }
    Text(text = text)
}
