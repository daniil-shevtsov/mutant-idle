package com.daniil.shevtsov.idle.feature.location.domain

import com.daniil.shevtsov.idle.feature.main.domain.PlotHolder
import com.daniil.shevtsov.idle.feature.main.domain.Selectable
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation

data class Location(
    override val id: Long,
    override val title: String,
    val description: String,
    val tags: Map<TagRelation, List<Tag>>,
    override val plot: String?,
) : Selectable, PlotHolder {
    override fun createDefaultPlot(): String = "You went to the $title"
    override fun copy(plot: String?): PlotHolder = this@Location.copy(id = id, plot = plot)
    override fun copy(
        id: Long?,
        title: String?,
    ): Selectable = copy(
        id = id ?: this@Location.id,
        title = title ?: this@Location.title,
    )
}
