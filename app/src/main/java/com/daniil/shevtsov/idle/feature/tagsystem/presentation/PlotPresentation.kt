package com.daniil.shevtsov.idle.feature.tagsystem.presentation

import com.daniil.shevtsov.idle.feature.main.presentation.Group
import com.daniil.shevtsov.idle.feature.plot.domain.PlotEntry
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.Plot
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.plot

fun presentPlot(plot: List<Plot>, reversed: Boolean = true): List<Plot> {
    val squashed = countSequentialDuplicates(plot)
        .map { group ->
            when {
                group.count > 1 -> "${group.value} (x${group.count})"
                else -> group.value
            }
        }
    return when(reversed) {
        true -> squashed.reversed()
        else -> squashed
    }
}

private fun countSequentialDuplicates(values: List<String>): List<Group> {
    val groups = mutableListOf<Group>()
    values.forEach {
        val last = groups.lastOrNull()
        if (last?.value == it) {
            last.count++
        } else {
            groups.add(Group(it, 1))
        }
    }
    return groups
}
