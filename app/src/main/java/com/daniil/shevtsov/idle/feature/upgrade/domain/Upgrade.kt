package com.daniil.shevtsov.idle.feature.upgrade.domain

data class Upgrade(
    val id: Long,
    val title: String,
    val subtitle: String,
    val price: Price,
    val status: UpgradeStatus,
)