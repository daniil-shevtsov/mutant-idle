package com.daniil.shevtsov.idle.feature.tagsystem.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.idle.feature.tagsystem.domain.createDefaultTags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.lines

@Preview
@Composable
fun SpikeTagScreenPreview() {
    SpikeTagScreen()
}

@Composable
fun SpikeTagScreen(modifier: Modifier = Modifier) {
    val currentTags = createDefaultTags()
    val possibleActions = lines.flatMap {
        it.requiredTags.entries.filter { it.key == "current action" }.map { it.value }.distinct()
    }
    val possibleTags = lines.flatMap {
        it.requiredTags.keys
    }.distinct().filter { it != "current action" }
    val possibleValues = lines.flatMap { it.requiredTags.map { it.value } }.distinct()

    Column {
        Text(
            text = "plot\nplot\n" +
                    "plot\n" +
                    "plot",
            maxLines = 3,
        )
        Row {
            Column {
                Text("Current Tags:")
                currentTags.forEach { tag ->
                    Text(text = tag.key + ": " + tag.value)
                }
            }
            Column {
                Text("Actions:")
                possibleActions.forEach { action ->
                    Text(action)
                }
            }
            Column {
                Text("Debug tags:")
                possibleTags.forEach { action ->
                    Text(action)
                }
            }
            Column {
                Text("Debug tag values:")
                possibleValues.forEach { action ->
                    Text(action)
                }
            }
        }
    }


}
