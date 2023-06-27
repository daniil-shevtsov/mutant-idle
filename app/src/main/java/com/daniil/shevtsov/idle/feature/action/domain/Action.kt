package com.daniil.shevtsov.idle.feature.action.domain

import com.daniil.shevtsov.idle.feature.main.domain.PlotHolder
import com.daniil.shevtsov.idle.feature.main.domain.Selectable
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation

typealias ResourceChanges = Map<ResourceKey, Double>
typealias RatioChangeForTags = Map<List<Tag>, Double>
typealias RatioChanges = Map<RatioKey, RatioChangeForTags>

data class Action(
    override val id: Long,
    override val title: String,
    val subtitle: String,
    val resourceChanges: ResourceChanges,
    val ratioChanges: RatioChanges,
    val tags: Map<TagRelation, List<Tag>>,
    override val plot: String?,
) : Selectable, PlotHolder {
    override fun createDefaultPlot(): String = "You performed action \"$title\""
    override fun copy(plot: String?): PlotHolder = this.copy(id = id, plot = plot)
    override fun copy(
        id: Long?,
        title: String?,
    ): Selectable = copy(
        id = id ?: this@Action.id,
        title = title ?: this@Action.title,
    )
}
