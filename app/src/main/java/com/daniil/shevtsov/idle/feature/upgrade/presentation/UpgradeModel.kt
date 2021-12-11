package com.daniil.shevtsov.idle.feature.upgrade.presentation

data class UpgradeModel(
    val id: Long,
    val title: String,
    val subtitle: String,
    val price: PriceModel,
    val status: UpgradeStatusModel = UpgradeStatusModel.Affordable,
)