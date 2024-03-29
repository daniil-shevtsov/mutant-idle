package com.daniil.shevtsov.idle.feature.upgrade.domain

import com.daniil.shevtsov.idle.feature.action.domain.RatioChanges
import com.daniil.shevtsov.idle.feature.main.domain.PlotHolder
import com.daniil.shevtsov.idle.feature.main.domain.Selectable
import com.daniil.shevtsov.idle.feature.main.presentation.Flavorable
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceChanges
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelations

data class Upgrade(
    override val id: Long,
    override val title: String,
    override val subtitle: String,
    val price: Price, //TODO: Is price used for anything or it's a relic from the time before resource changes?
    val resourceChanges: ResourceChanges, //TODO: Need to be able define alternative resources (scrap for android, blood for vampire)
    val ratioChanges: RatioChanges,
    val status: UpgradeStatus,
    override val tagRelations: TagRelations,
    override val plot: String?,
) : Selectable, Flavorable, PlotHolder {
    override fun createDefaultPlot(): String = "You have gained an upgrade \"$title\""

    override fun copy(plot: String?): PlotHolder = this@Upgrade.copy(id = id, plot = plot)
    override fun copy(
        id: Long?,
        title: String?,
        tagRelations: TagRelations?,
    ): Selectable = copy(
        id = id ?: this@Upgrade.id,
        title = title ?: this@Upgrade.title,
        tagRelations = tagRelations ?: this@Upgrade.tagRelations,
    )

    override fun copy(
        title: String?,
        subtitle: String?,
        plot: String?,
    ): Flavorable = copy(
        title = title ?: this@Upgrade.title,
        subtitle = subtitle ?: this@Upgrade.subtitle,
        plot = plot ?: this@Upgrade.plot,
    )

}
