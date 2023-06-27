package com.daniil.shevtsov.idle.feature.location.domain

import com.daniil.shevtsov.idle.feature.main.domain.PlotHolder
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation

data class Location(
    val id: Long,
    val title: String,
    val description: String,
    val tags: Map<TagRelation, List<Tag>>,
    override val plot: String,
) : PlotHolder {
    override fun createDefaultPlot(): String = "You went to the $title"
}
