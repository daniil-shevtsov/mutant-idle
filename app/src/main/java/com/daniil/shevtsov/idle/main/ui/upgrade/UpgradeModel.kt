package com.daniil.shevtsov.idle.main.ui.upgrade

data class UpgradeModel(
    val id: Long,
    val title: String,
    val subtitle: String,
    val status: UpgradeStatusModel = UpgradeStatusModel.Affordable,
)