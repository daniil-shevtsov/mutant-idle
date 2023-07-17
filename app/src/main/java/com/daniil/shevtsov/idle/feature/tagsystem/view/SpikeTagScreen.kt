package com.daniil.shevtsov.idle.feature.tagsystem.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme
import com.daniil.shevtsov.idle.feature.tagsystem.domain.createDefaultTags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.game
import com.daniil.shevtsov.idle.feature.tagsystem.domain.newPerform
import com.daniil.shevtsov.idle.feature.tagsystem.domain.plotLines
import com.daniil.shevtsov.idle.feature.tagsystem.domain.spikeTag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.withAdditional

@Preview
@Composable
fun SpikeTagScreenPreview() {
    SpikeTagScreen()
}

@Composable
fun SpikeTagScreen(modifier: Modifier = Modifier) {
    var currentGame by remember {
        mutableStateOf(
            game(
                tags = createDefaultTags().withAdditional("ability" to "flight")
            )
        )
    }
    var selectedTag by remember { mutableStateOf<String?>("mobile") }

    val possibleActions = plotLines.flatMap {
        it.requiredTags.entries.filter { it.key.tagKey == "current action" }.map { it.value.value }
    }.distinct() + "wait"
    val possibleTags = plotLines.flatMap {
        it.requiredTags.keys
    }.distinct().filter { it.tagKey != "current action" }
    val possibleValues = plotLines.flatMap { it.requiredTags.map { it.value.value } }.distinct()

    Column(modifier = modifier.background(AppTheme.colors.background)) {
        Text(
            text = currentGame.plot.joinToString(separator = "\n"),
            modifier = Modifier.background(Color.White).padding(4.dp)
                .fillMaxWidth()
                .height(100.dp)
                .verticalScroll(rememberScrollState())
        )
        Row {
            Column {
                Title("Current Tags:")
                currentGame.tags.values.forEach { tag ->
                    Tag(
                        text = tag.key.tagKey + ": " + tag.value,
                        isSelected = tag.key.tagKey == selectedTag,
                        modifier = Modifier.clickable {
                            selectedTag = tag.key.tagKey
                        }
                    )
                }
            }
            Column {
                Title("Actions:")
                possibleActions.forEach { action ->
                    Tag(action, modifier = Modifier.clickable {
                        val newGame = newPerform(currentGame, action)
                        currentGame = newGame
                    })
                }
            }
            Column {
                Title("Debug tags:")
                possibleTags.forEach { tag ->
                    Tag(tag.tagKey, modifier = Modifier.clickable {
                        if (!currentGame.tags.containsKey(tag)) {
                            currentGame = currentGame.copy(
                                tags = currentGame.tags + mapOf(
                                    tag to spikeTag(
                                        key = tag,
                                        value = "null"
                                    )
                                )
                            )

                        }
                    })
                }
            }
            Column {
                Title("Debug tag values:")
                possibleValues.forEach { tagValue ->
                    Tag(tagValue, modifier = Modifier.clickable {
                        currentGame = currentGame.copy(
                            tags = currentGame.tags.values.map { tag ->
                                when (tag.key.tagKey) {
                                    selectedTag -> tag.key to tag.copy(
                                        value = tagValue
                                    )

                                    else -> tag.key to tag
                                }
                            }.toMap()
                        )
                    })
                }
            }
        }
    }


}

@Composable
private fun Title(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text,
        modifier = modifier,
        style = AppTheme.typography.bodyTitle,
        color = AppTheme.colors.textLight
    )
}

@Composable
private fun Tag(
    text: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    Text(
        text,
        modifier = modifier.let {
            when {
                isSelected -> it.background(AppTheme.colors.backgroundLight).padding(2.dp)
                else -> it.padding(2.dp)
            }
        }.background(AppTheme.colors.background),
        style = AppTheme.typography.body,
        color = AppTheme.colors.textLight
    )
}
