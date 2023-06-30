package com.daniil.shevtsov.idle.feature.action.domain

import com.daniil.shevtsov.idle.feature.main.domain.PlotHolder
import com.daniil.shevtsov.idle.feature.main.domain.Selectable
import com.daniil.shevtsov.idle.feature.main.presentation.Flavorable
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation

typealias ResourceChanges = Map<ResourceKey, Double>
typealias RatioChangeForTags = Map<List<Tag>, Double>
typealias RatioChanges = Map<RatioKey, RatioChangeForTags>
typealias TagRelations = Map<TagRelation, List<Tag>>

data class Action(
    override val id: Long,
    override val title: String,
    override val subtitle: String,
    val resourceChanges: ResourceChanges,
    val ratioChanges: RatioChanges,
    val tags: TagRelations,
    override val plot: String?,
) : Selectable, Flavorable, PlotHolder {
    override fun createDefaultPlot(): String = "You performed action \"$title\""
    override fun copy(plot: String?): PlotHolder = this.copy(id = id, plot = plot)
    override fun copy(
        id: Long?,
        title: String?,
    ): Selectable = copy(
        id = id ?: this@Action.id,
        title = title ?: this@Action.title,
    )

    override fun copy(
        title: String?,
        subtitle: String?,
        plot: String?,
    ): Flavorable = copy(
        title = title ?: this@Action.title,
        subtitle = subtitle ?: this@Action.subtitle,
        plot = plot ?: this@Action.plot,
    )
}
