package com.daniil.shevtsov.idle.feature.upgrade.domain

import com.daniil.shevtsov.idle.feature.action.domain.RatioChanges
import com.daniil.shevtsov.idle.feature.action.domain.ResourceChanges
import com.daniil.shevtsov.idle.feature.main.domain.PlotHolder
import com.daniil.shevtsov.idle.feature.main.domain.Selectable
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation

data class Upgrade(
    override val id: Long,
    override val title: String,
    val subtitle: String,
    val price: Price, //TODO: Is price used for anything or it's a relic from the time before resource changes?
    val resourceChanges: ResourceChanges, //TODO: Need to be able define alternative resources (scrap for android, blood for vampire)
    val ratioChanges: RatioChanges,
    val status: UpgradeStatus,
    val tags: Map<TagRelation, List<Tag>>,
    override val plot: String?,
) : Selectable, PlotHolder {
    override fun createDefaultPlot(): String = "You have gained an upgrade \"$title\""

    override fun copy(plot: String?): PlotHolder = this@Upgrade.copy(id = id, plot = plot)
    override fun copy(
        id: Long?,
        title: String?,
    ): Selectable = copy(
        id = id ?: this@Upgrade.id,
        title = title ?: this@Upgrade.title,
    )
}
