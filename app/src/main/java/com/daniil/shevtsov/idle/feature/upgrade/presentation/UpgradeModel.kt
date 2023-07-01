package com.daniil.shevtsov.idle.feature.upgrade.presentation

import com.daniil.shevtsov.idle.feature.action.presentation.RatioChangeModel
import com.daniil.shevtsov.idle.feature.action.presentation.ResourceChangeModel

data class UpgradeModel(
    override val id: Long,
    override val title: String,
    override val subtitle: String,
    val price: PriceModel,
    val status: UpgradeStatusModel = UpgradeStatusModel.Affordable,
    val resourceChanges: List<ResourceChangeModel>,
    val ratioChanges: List<RatioChangeModel>,
) : FlavoredModel, SelectableModel

interface FlavoredModel {
    val id: Long
    val title: String
    val subtitle: String
}

interface SelectableModel {
    val id: Long
    val title: String
}
