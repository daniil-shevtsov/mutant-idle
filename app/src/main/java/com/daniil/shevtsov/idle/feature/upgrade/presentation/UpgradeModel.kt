package com.daniil.shevtsov.idle.feature.upgrade.presentation

import com.daniil.shevtsov.idle.feature.action.presentation.RatioChangeModel
import com.daniil.shevtsov.idle.feature.action.presentation.ResourceChangeModel

data class UpgradeModel(
    val id: Long,
    val title: String,
    val subtitle: String,
    val price: PriceModel,
    val status: UpgradeStatusModel = UpgradeStatusModel.Affordable,
    val resourceChanges: List<ResourceChangeModel>,
    val ratioChanges: List<RatioChangeModel>,
)
