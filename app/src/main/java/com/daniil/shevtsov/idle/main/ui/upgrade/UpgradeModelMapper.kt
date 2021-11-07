package com.daniil.shevtsov.idle.main.ui.upgrade

import com.daniil.shevtsov.idle.main.domain.upgrade.Upgrade

internal object UpgradeModelMapper {
    fun map(upgrade: Upgrade, status: UpgradeStatusModel) = with(upgrade) {
        UpgradeModel(
            id = id,
            title = title,
            subtitle = subtitle,
            price = PriceModel(value = price.value.toString())
        )
    }
}