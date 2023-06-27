package com.daniil.shevtsov.idle.feature.upgrade.domain

import com.daniil.shevtsov.idle.feature.action.domain.RatioChanges
import com.daniil.shevtsov.idle.feature.action.domain.ResourceChanges
import com.daniil.shevtsov.idle.feature.main.domain.PlotHolder
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation

data class Upgrade(
    val id: Long,
    val title: String,
    val subtitle: String,
    val price: Price,
    val resourceChanges: ResourceChanges,
    val ratioChanges: RatioChanges,
    val status: UpgradeStatus,
    val tags: Map<TagRelation, List<Tag>>,
    override val plot: String?,
) : PlotHolder {
    override fun createDefaultPlot(): String = "You have gained an upgrade \"$title\""
    override fun copy(plot: String?): PlotHolder = this@Upgrade.copy(id = id, plot = plot)
}
