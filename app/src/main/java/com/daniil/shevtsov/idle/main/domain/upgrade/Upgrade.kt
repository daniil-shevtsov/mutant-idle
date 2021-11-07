package com.daniil.shevtsov.idle.main.domain.upgrade

data class Upgrade(
    val id: Long,
    val title: String,
    val subtitle: String,
    val price: Price,
    val status: UpgradeStatus,
)