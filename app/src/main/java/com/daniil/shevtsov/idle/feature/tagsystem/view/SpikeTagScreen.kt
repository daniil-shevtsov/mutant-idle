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
import com.daniil.shevtsov.idle.feature.tagsystem.domain.lines
import com.daniil.shevtsov.idle.feature.tagsystem.domain.perform
import com.daniil.shevtsov.idle.feature.tagsystem.domain.spikeTag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.withAdditional

@Preview
@Composable
fun SpikeTagScreenPreview() {
    SpikeTagScreen()
}

@Composable
fun SpikeTagScreen(modifier: Modifier = Modifier) {
    var currentTags by remember { mutableStateOf(createDefaultTags().withAdditional("ability" to "flight")) }
    var currentPlot by remember { mutableStateOf("") }
    var selectedTag by remember { mutableStateOf<String?>("mobile") }

    val possibleActions = lines.flatMap {
        it.requiredTags.entries.filter { it.key.tagKey == "current action" }.map { it.value.value}
    }.distinct() + "wait"
    val possibleTags = lines.flatMap {
        it.requiredTags.keys
    }.distinct().filter { it.tagKey != "current action" }
    val possibleValues = lines.flatMap { it.requiredTags.map { it.value } }.distinct()

    Column(modifier = modifier.background(AppTheme.colors.background)) {
        Text(
            text = currentPlot,
            modifier = Modifier.background(Color.White).padding(4.dp)
                .fillMaxWidth()
                .height(100.dp)
                .verticalScroll(rememberScrollState())
        )
        Row {
            Column {
                Title("Current Tags:")
                currentTags.forEach { tag ->
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
                        val result = perform(currentTags.withAdditional("current action" to action))
                        currentTags = result.tags
                        currentPlot = result.plot[0] + "\n" + currentPlot
                    })
                }
            }
            Column {
                Title("Debug tags:")
                possibleTags.forEach { tag ->
                    Tag(tag.tagKey, modifier = Modifier.clickable {
                        if (!currentTags.containsKey(tag)) {
                            currentTags += tag to spikeTag(key = tag, value = "null")
                        }
                    })
                }
            }
            Column {
                Title("Debug tag values:")
                possibleValues.forEach { tag ->
                    Tag(tag.value, modifier = Modifier.clickable {
                        currentTags = currentTags.map { entry ->
                            when (entry.key.tagKey) {
                                selectedTag -> entry.key to tag
                                else -> entry.key to entry.value
                            }
                        }.toMap()
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
