package com.daniil.shevtsov.idle.feature.action.domain

import com.daniil.shevtsov.idle.feature.main.domain.PlotHolder
import com.daniil.shevtsov.idle.feature.main.domain.Selectable
import com.daniil.shevtsov.idle.feature.main.presentation.Flavorable
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceChanges
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelations

typealias RatioChangeForTags = Map<List<Tag>, Double>
typealias RatioChanges = Map<RatioKey, RatioChangeForTags>

data class Action(
    override val id: Long,
    override val title: String,
    override val subtitle: String,
    override val tagRelations: TagRelations,
    val resourceChanges: ResourceChanges,
    val ratioChanges: RatioChanges,
    override val plot: String?,
) : Selectable, Flavorable, PlotHolder {
    override fun createDefaultPlot(): String = "You performed action \"$title\""
    override fun copy(plot: String?): PlotHolder = this.copy(id = id, plot = plot)
    override fun copy(
        id: Long?,
        title: String?,
        tagRelations: TagRelations?,
    ): Selectable = copy(
        id = id ?: this@Action.id,
        title = title ?: this@Action.title,
        tagRelations = tagRelations ?: this@Action.tagRelations,
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
