package com.daniil.shevtsov.idle.feature.upgrade.presentation

import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade

internal object UpgradeModelMapper {
    fun map(upgrade: Upgrade, status: UpgradeStatusModel) = with(upgrade) {
        UpgradeModel(
            id = id,
            title = title,
            subtitle = subtitle,
            price = PriceModel(value = price.value.toString()),
            status = status,
        )
    }
}